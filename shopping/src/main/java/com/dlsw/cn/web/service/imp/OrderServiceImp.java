package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.web.configuration.WxPayProperties;
import com.dlsw.cn.dto.OrderDTO;
import com.dlsw.cn.web.mapper.OrderMapper;
import com.dlsw.cn.web.mapper.WxPayOrderNotifyMapper;
import com.dlsw.cn.web.service.BaseService;
import com.dlsw.cn.web.service.OrderService;
import com.dlsw.cn.util.DateUtil;
import com.dlsw.cn.web.vo.OrderVo;
import com.dlsw.cn.web.vo.PayCertificateVo;
import com.dlsw.cn.enumerate.*;
import com.dlsw.cn.po.*;
import com.dlsw.cn.repositories.*;
import com.github.binarywang.wxpay.bean.request.WxPayBaseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class OrderServiceImp extends BaseService implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final static int QUASI_SUPERFINE_THRESHOLD = 4;
    public final static int SUPERFINE_THRESHOLD = 6;
    public final static String PAY_NOTICE = "http://www.jinhuishengwu.cn/api/wechat/pay/payNotice";
    @Resource(name = "wxPayService")
    private WxPayService wxPayService;
    @Autowired
    private WxPayProperties wxPayProperties;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    private RebateRepository rebateRepository;
    @Autowired
    private WxPayOrderNotifyMapper notifyMapper;

    private OrderCheck orderCheck = new OrderCheck();

    private RebateAction rebateAction = new RebateAction();

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO applyOrder(String phone, OrderVo orderVo) {
        User user = userRepository.findByPhone(phone);
        Product product = productRepository.findOne(orderVo.getProductId());
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIdAndUser(orderVo.getDeliverAddressId(), user);
        User recommend_man = userRepository.findByPhone(orderVo.getRecommendPhone());
        orderCheck.applyOrder(user, product, recommend_man, deliveryAddress, orderVo);
        int piece;
        BigDecimal price;
        if (product.getProductType() == ProductType.套餐产品) {
            piece = product.getPiece();
            price = product.getRetailPrice();
        } else {
            piece = orderVo.getProductNum();
            price = getProductPrice(user.getRoleType(), product);
        }
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(piece));
        Order order = saveOrder(orderVo, user, product, deliveryAddress, piece, price, totalCost);
        order.getHigherUserList().add(recommend_man);
        recommend_man.getServiceOrderList().add(order);
        userRepository.save(recommend_man);
        return orderMapper.orderToOrderDTO(order);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO savePayCert(String phone, PayCertificateVo payCertificateVo) {
        Order order = orderRepository.findOne(payCertificateVo.getOrderId());
        orderCheck.savePayCert(order,payCertificateVo);
        String[] payCentPhoto = payCertificateVo.getPayCertPhoto();
        order.setPayCertPhoto(StringUtils.join(payCentPhoto, ","));
        order.setPayCertInfo(payCertificateVo.getPayCertInfo());
        order.setOrderStatus(OrderStatus.待确认);
        order = orderRepository.save(order);
        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
        return orderDTO;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public void payWsSuccess(String orderCode, WxPayOrderNotifyResult result) {
        Order order = orderRepository.findOneByOrderCode(orderCode);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getPayWay() != PayType.余额支付) {
            throw new RuntimeException("支付订单,必须是余额支付类型");
        }
        WxPayOrderNotify wxPayOrderNotify = notifyMapper.WxPayOrderNotifyResultToWxPayOrderNotify(result);
        order.setWxPayOrderNotify(wxPayOrderNotify);
        wxPayOrderNotify.setOrder(order);
        User user = order.getUser();
        User recommendMan = userRepository.findByPhone(order.getRecommendPhone());
        orderRepository.updateOrderStatusByIdAndUser(OrderStatus.已支付, order.getId(), user);
        Product product = productRepository.getProductByproductCode(order.getProductCode());
        setPayRoleType(user, product);
        if (user.getHigher() == null && user.getRoleType().getCode() > RoleType.普通.getCode()) {
            List<User> offspringUser = userRepository.findByLikeOrgPath(getLikeStr(user));
            if (offspringUser != null && offspringUser.size() > 0) {
                offspringUser.forEach(lower -> {
                    lower.setOrgPath(bindOffSpringOrgPath(recommendMan, lower));
                    userRepository.save(lower);
                });
            }
            recommendMan.getLower().add(user);
            user.setHigher(recommendMan);
            user.setLevel(recommendMan.getLevel() + 1);
            user.setOrgPath(bindOffSpringOrgPath(recommendMan, user));
        }
        userRepository.save(order.getUser());
    }

    @Override
    public Map payOrder(long orderId, String ipAddress) {
        Order order = orderRepository.findOne(orderId);
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOpenid(order.getUser().getOAuthInfo().getOpenId());
            orderRequest.setBody(order.getProductName() + "订单支付");
            orderRequest.setOutTradeNo(order.getOrderCode());
            orderRequest.setAppid(wxPayProperties.getAppId());
            orderRequest.setMchId(wxPayProperties.getMchId());
            orderRequest.setTotalFee(WxPayBaseRequest.yuanToFee(order.getProductCost().toString()));//元转成分
            orderRequest.setSpbillCreateIp(ipAddress);
            orderRequest.setTradeType("JSAPI");
            orderRequest.setNotifyURL(PAY_NOTICE);
            Map wxPayUnifiedOrderResult = wxPayService.getPayInfo(orderRequest);
            logger.debug(wxPayUnifiedOrderResult.toString());
            return wxPayUnifiedOrderResult;
        } catch (Exception e) {
            String error = "微信支付失败！订单号：{" + order.getOrderCode() + "},原因:{" + e.getMessage() + "}";
            logger.error(error);
            throw new RuntimeException(error);
        }
    }

    private void setPayRoleType(User user, Product product) {
        if (product.getProductType() == ProductType.套餐产品 && product.getRoleType().getCode() > user.getRoleType().getCode()) {
            if (user.getAuthorizationCode() == null) {
                user.setAuthorizationCode(generateAuthCode());
            }
            user.setRoleType(product.getRoleType());
        }
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO sureOrder(String phone, long orderId) {
        User user = userRepository.findByPhone(phone);
        Order order = orderRepository.findOne(orderId);
        User orderUser = order.getUser();
        orderCheck.sureOrder(order, user, orderUser);
        //修改订单状态 为已支付
        order.setOrderStatus(OrderStatus.已支付);
        orderRepository.save(order);
        //更新订单人级别,如果是套餐 并且 套餐的产品级别大于用户级别 ，修改用户级别
        Product product = productRepository.getProductByproductCode(order.getProductCode());
        if (product.getProductType() == ProductType.套餐产品 && product.getRoleType().getCode() > user.getRoleType().getCode()) {
            if (user.getAuthorizationCode() == null) {
                user.setAuthorizationCode(generateAuthCode());
            }
            user.setRoleType(product.getRoleType());
        }
        //当用户没有上级，切用户级别 非普通用户,则将用户以及其下属挂到自己下面
        if (orderUser.getHigher() == null && orderUser.getRoleType().getCode() > RoleType.普通.getCode()) {
            //更新子孙节点Path
            List<User> grandUserList = userRepository.findByLikeOrgPath(getLikeStr(orderUser));
            if (grandUserList != null && grandUserList.size() > 0) {
                grandUserList.forEach(lower -> {
                    lower.setOrgPath(bindOffSpringOrgPath(user, lower));
                    userRepository.save(lower);
                });
            }
            user.getLower().add(orderUser);
            orderUser.setHigher(user);
            orderUser.setLevel(user.getLevel() + 1);
            orderUser.setOrgPath(bindOffSpringOrgPath(user, orderUser));
            userRepository.save(orderUser);
            //查看直系下属的合伙人的数量，满足4个人升级准合伙人，6个特级合伙人
            List<User> seniorList = user.getLower().stream().filter(lowerUser -> lowerUser.getRoleType().getCode() >= RoleType.合伙人.getCode()).collect(Collectors.toList());
            if (seniorList.size() == QUASI_SUPERFINE_THRESHOLD) {
                user.setRoleType(RoleType.准特级合伙人);
                userRepository.save(user);
            } else if (seniorList.size() == SUPERFINE_THRESHOLD) {
                user.setRoleType(RoleType.特级合伙人);
                userRepository.save(user);
            }

            rebateAction.expand(user, order);
        }
        rebateAction.sale(user, order);
        return orderMapper.orderToOrderDTO(order);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    private Order saveOrder(OrderVo orderVo, User user, Product product, DeliveryAddress deliveryAddress, int piece, BigDecimal price, BigDecimal totalCost) {
        Order order = new Order();
        order.setOrderCode(generateOrderCode(String.valueOf(user.getId())));
        order.setUser(user);
        order.setOrderStatus(OrderStatus.待支付);
        order.setOrderTime(DateUtil.getCurrentDate());
        order.setOrderComment(orderVo.getOrderComment());
        order.setPayWay(orderVo.getPayType());
        order.setProductName(product.getProductName());
        order.setProductCode(product.getProductCode());
        order.setProductNum(piece);
        order.setProductPrice(price);
        order.setProductCost(totalCost);
        order.setReceiverProvince(deliveryAddress.getProvince());
        order.setReceiverCity(deliveryAddress.getCity());
        order.setReceiverRegion(deliveryAddress.getRegion());
        order.setReceiverDetailed(deliveryAddress.getDetailed());
        order.setReceiverPhone(deliveryAddress.getPhone());
        order.setReceiverName(deliveryAddress.getDeliveryMan());
        order.setSendName(product.getSendMan());
        order.setSendManHead(product.getSendMan());
        order.setSendPhone(product.getSendPhone());
        order.setRecommendPhone(orderVo.getRecommendPhone());
        Optional<Set<Order>> orderList = Optional.ofNullable(user.getOrderList());
        orderList.orElse(new TreeSet<>());
        orderList.get().add(order);
        order.setUser(user);
        return orderRepository.save(order);
    }

    @Override
    public OrderDTO getOrder(String phone, long orderId) {
        Order order = orderRepository.findOneById(orderId);
        return orderMapper.orderToOrderDTO(order);
    }

    @Override
    public Integer getOrderCount(String phone, OrderType orderType) {
        User user = userRepository.findByPhone(phone);
        Set<Order> orderList;
        if (orderType == OrderType.进货订单) {
            orderList = user.getOrderList();
        } else {
            orderList = user.getServiceOrderList();
        }
        return orderList.size();
    }

    @Override
    public BigDecimal getProductPrice(RoleType roleType, Product product) {
        if (roleType == RoleType.VIP && product.getPrice1() != null) {
            return product.getPrice1();
        } else if (roleType == RoleType.合伙人 && product.getPrice2() != null) {
            return product.getPrice2();
        } else if (roleType == RoleType.高级合伙人 && product.getPrice3() != null) {
            return product.getPrice3();
        } else if (roleType == RoleType.准特级合伙人 && product.getPrice4() != null) {
            return product.getPrice4();
        } else if (roleType == RoleType.特级合伙人 && product.getPrice4() != null) {
            return product.getPrice4();
        }
        return product.getRetailPrice();
    }

    @Override
    public List<OrderDTO> getOrderList(OrderType orderType, OrderStatus status, String phone) {
        User user = userRepository.findByPhone(phone);
        Set<Order> orderList;
        if (orderType == OrderType.进货订单) {
            orderList = user.getOrderList();
        } else {
            orderList = user.getServiceOrderList();
        }
        List orderFilterList;
        if (status != OrderStatus.全部订单) {
            orderFilterList = orderList.stream().filter(order -> order.getOrderStatus() == status).collect(Collectors.toList());
        } else {
            orderFilterList = orderList.stream().collect(Collectors.toList());
        }
        List<OrderDTO> orderDTOList = orderMapper.orderToOrderDTOList(orderFilterList);
        orderDTOList.forEach(orderDTO -> orderDTO.setOrderType(orderType.getName()));
        return orderDTOList;
    }

    @Override
    public Map<String, Long> summaryOrderCount(String phone, OrderType orderType) {
        User user = userRepository.findByPhone(phone);
        Set<Order> orderList;
        if (orderType == OrderType.进货订单) {
            orderList = user.getOrderList();
        } else {
            orderList = user.getServiceOrderList();
        }
        Map<Integer, Long> groupResult = orderList.stream().collect(Collectors.groupingBy(order -> order.getOrderStatus().getCode(), Collectors.counting()));
        Map<String, Long> map = new HashMap<>();
        long unPay = groupResult.get(OrderStatus.待支付.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.待支付.getCode());
        long alConfirm = groupResult.get(OrderStatus.待确认.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.待确认.getCode());
        long alPay = groupResult.get(OrderStatus.已支付.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已支付.getCode());
        long alSend = groupResult.get(OrderStatus.已发货.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已发货.getCode());
        long complete = groupResult.get(OrderStatus.已完成.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已完成.getCode());
        long cancel = groupResult.get(OrderStatus.已取消.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已取消.getCode());
        map.put("全部订单", Long.valueOf(unPay + alConfirm + alPay + alSend + complete + cancel));
        map.put(OrderStatus.待支付.getName(), unPay);
        map.put(OrderStatus.待确认.getName(), alConfirm);
        map.put(OrderStatus.已支付.getName(), alPay);
        map.put(OrderStatus.已发货.getName(), alSend);
        map.put(OrderStatus.已完成.getName(), complete);
        map.put(OrderStatus.已取消.getName(), cancel);
        return map;
    }

    private class OrderCheck {
        public void applyOrder(User user, Product product, User recommend_man, DeliveryAddress deliveryAddress, OrderVo orderVo) {
            if (user.isWeUser() && !user.isVerificationPhone()) {
                throw new RuntimeException("您是微信用户还未验证过手机，请先设置手机");
            }
            if (product == null) {
                throw new RuntimeException("无法找到对应的商品");
            }
            if (deliveryAddress == null) {
                throw new RuntimeException("无法找到对应的收货地址");
            }
            if (orderVo.getPayType() == PayType.线下转账 && StringUtils.isBlank(orderVo.getRecommendPhone())) {
                throw new RuntimeException("线下订单推荐人不能为空");
            }
            if (orderVo.getRecommendPhone().equals(user.getPhone())) {
                throw new RuntimeException("推荐人不能是本人");
            }
            if (userRepository.findOffspringCountByOrgPathAndPhone(getLikeStr(user), orderVo.getRecommendPhone()) != null) {
                throw new RuntimeException("推荐人不能是自己的下属");
            }
            if (user.getHigher() != null && !user.getHigher().getPhone().equals(orderVo.getRecommendPhone())) {
                throw new RuntimeException("推荐人必须是自己的上级,您的上级是(" + user.getHigher().getPhone() + ")");
            }
            if (recommend_man == null) {
                throw new RuntimeException("推荐人没有找到");
            }
            if (recommend_man.getRoleType() == RoleType.普通) {
                throw new RuntimeException("该推荐人不是合伙人");
            }
            if (recommend_man.isWeUser() && !recommend_man.isVerificationPhone()) {
                throw new RuntimeException("你的推荐人还未验证过手机，无法填写");
            }
        }

        public void savePayCert(Order order, PayCertificateVo payCertificateVo) {
            if (order == null) {
                throw new RuntimeException("订单不存在");
            }
            if (order.getOrderStatus() != OrderStatus.待支付 && order.getOrderStatus() != OrderStatus.待确认) {
                throw new RuntimeException("订单状态必须是待支付");
            }
            if (order.getPayWay() != PayType.线下转账) {
                throw new RuntimeException("上传凭证，必须是线下订单类型");
            }
            if (StringUtils.isBlank(order.getRecommendPhone())) {
                throw new RuntimeException("线下订单推荐人不能为空");
            }
            User recommend_man = userRepository.findByPhone(order.getRecommendPhone());
            if (recommend_man == null) {
                throw new RuntimeException("推荐人没有找到");
            }
            if (payCertificateVo.getPayCertPhoto() == null || payCertificateVo.getPayCertPhoto().length == 0) {
                throw new RuntimeException("凭证照片不能为空");
            }
            if (StringUtils.isBlank(payCertificateVo.getPayCertInfo())) {
                throw new RuntimeException("凭证信息不能为空");
            }
        }

        public void sureOrder(Order order, User user, User orderUser) {
            //线下转账，由属确认支付
            if (StringUtils.isBlank(order.getRecommendPhone()) || !user.getPhone().equals(order.getRecommendPhone())) {
                throw new RuntimeException("对不起，线下转账为成功，需要推荐人确认支付");
            }
            if (order.getPayWay() != PayType.线下转账) {
                throw new RuntimeException("确认订单，必须是线下转账类型");
            }
            if (order.getOrderStatus() != OrderStatus.待确认) {
                throw new RuntimeException("订单状态必须是待确认");
            }
            if (orderUser.getHigher() != null && !user.getPhone().equals(orderUser.getHigher().getPhone())) {
                throw new RuntimeException("该用户已经有上级（" + orderUser.getHigher().getPhone() + "），先取消订单重新提交");
            }
        }
    }

    private class RebateAction {

        public void sale(User user, Order order) {
            //一代和三代返利奖励
            if ((user.getLevel().intValue() == LevelType.第一代.getCode() || user.getLevel().intValue() == LevelType.第三代.getCode()) && user.getRoleType().getCode() >= RoleType.特级合伙人.getCode()) {
                Rebate rebate = new Rebate();
                rebate.setOrder(order);
                rebate.setUser(user);
                if (user.getLevel() == LevelType.第一代.getCode()) {
                    rebate.setRebate(new BigDecimal(LevelType.第一代.getReward()).multiply(new BigDecimal(order.getProductNum())));
                    rebate.setReason("一代返利奖励");
                } else if (user.getLevel() == LevelType.第三代.getCode()) {
                    rebate.setRebate(new BigDecimal(LevelType.第三代.getReward()).multiply(new BigDecimal(order.getProductNum())));
                    rebate.setReason("三代返利奖励");
                }
                rebate.setRebateStatus(RebateStatus.未确认);
                order.setRebate(rebate);
                user.getRebateSet().add(rebate);
                rebateRepository.save(rebate);
            }
        }

        public void expand(User user, Order order) {
            //发展合伙人返利奖励
            if (user.getRoleType().getCode() == RoleType.准特级合伙人.getCode()) {
                Rebate rebate = new Rebate();
                rebate.setOrder(order);
                rebate.setUser(user);
                rebate.setRebate(new BigDecimal(30).multiply(new BigDecimal(order.getProductNum())));
                rebate.setReason("发展合伙人返利奖励");
                order.setRebate(rebate);
                user.getRebateSet().add(rebate);
                rebateRepository.save(rebate);
            }
        }
    }
}
