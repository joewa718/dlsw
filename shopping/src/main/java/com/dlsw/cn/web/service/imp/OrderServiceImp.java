package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.dto.OrderDTO;
import com.dlsw.cn.enumerate.*;
import com.dlsw.cn.po.*;
import com.dlsw.cn.repositories.DeliveryAddressRepository;
import com.dlsw.cn.repositories.OrderRepository;
import com.dlsw.cn.repositories.ProductRepository;
import com.dlsw.cn.repositories.UserRepository;
import com.dlsw.cn.util.DateUtil;
import com.dlsw.cn.util.GenerateRandomCode;
import com.dlsw.cn.web.configuration.WxPayProperties;
import com.dlsw.cn.web.mapper.OrderMapper;
import com.dlsw.cn.web.mapper.WxPayOrderNotifyMapper;
import com.dlsw.cn.web.service.BaseService;
import com.dlsw.cn.web.service.OrderCheckService;
import com.dlsw.cn.web.service.OrderService;
import com.dlsw.cn.web.vo.OrderVo;
import com.dlsw.cn.web.vo.PayCertificateVo;
import com.github.binarywang.wxpay.bean.request.WxPayBaseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${wechat.pay.payNotice}")
    public String payNotice;
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
    private WxPayOrderNotifyMapper notifyMapper;
    @Autowired
    private OrderCheckService orderCheckService;

    public String getIpAddress(HttpServletRequest request) {
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


    private DirectorLevel getDirectorLevel(User user) {
        long count = user.getLower().stream().filter(u -> u.getRoleType() == RoleType.高级合伙人).count();
        if (count >= 2 && count <= 3) {
            return DirectorLevel.仁;
        } else if (count >= 4 && count <= 5) {
            return DirectorLevel.义;
        } else if (count >= 6 && count <= 7) {
            return DirectorLevel.理;
        } else if (count >= 8 && count <= 9) {
            return DirectorLevel.智;
        } else if (count >= 10) {
            return DirectorLevel.信;
        }
        return DirectorLevel.无;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO applyOrder(String phone, OrderVo orderVo) {
        User user = userRepository.findByPhone(phone);
        Product product = productRepository.findOne(orderVo.getProductId());
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIdAndUser(orderVo.getDeliverAddressId(), user);
        //向上查找比自己级别高的用户
        User recommend_man = null;
        while (user.getHigher() == null) {
            User userHigher = user.getHigher();
            if (userHigher.getRoleType().getCode() > user.getRoleType().getCode()
                    || (userHigher.getRoleType() == user.getRoleType() && userHigher.getRoleType() == RoleType.高级合伙人)) {
                recommend_man = userHigher;
                break;
            }
        }
        if(recommend_man == null){
            new RuntimeException("未找到匹配的上级代理商");
        }
        orderCheckService.applyOrder(user, product, recommend_man, deliveryAddress, orderVo);
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
        while (recommend_man != null) {
            recommend_man.getHigher();
        }
        recommend_man.getServiceOrderList().add(order);
        userRepository.save(recommend_man);
        return orderMapper.orderToOrderDTO(order);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO savePayCert(String phone, PayCertificateVo payCertificateVo) {
        Order order = orderRepository.findOne(payCertificateVo.getOrderId());
        orderCheckService.savePayCert(order, payCertificateVo);
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
            orderRequest.setNotifyURL(payNotice);
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
                user.setAuthorizationCode(GenerateRandomCode.generateAuthCode());
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
        orderCheckService.sureOrder(order, user, orderUser);
        //修改订单状态 为已支付
        order.setOrderStatus(OrderStatus.已支付);
        orderRepository.save(order);
        //更新订单人级别,如果是套餐 并且 套餐的产品级别大于用户级别 ，修改用户级别
        Product product = productRepository.getProductByproductCode(order.getProductCode());
        if (product.getProductType() == ProductType.套餐产品 && product.getRoleType().getCode() > user.getRoleType().getCode()) {
            if (user.getAuthorizationCode() == null) {
                user.setAuthorizationCode(GenerateRandomCode.generateAuthCode());
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
        }
        return orderMapper.orderToOrderDTO(order);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public Order saveOrder(OrderVo orderVo, User user, Product product, DeliveryAddress deliveryAddress, int piece, BigDecimal price, BigDecimal totalCost) {
        Order order = new Order();
        order.setOrderCode(GenerateRandomCode.generateOrderCode(String.valueOf(user.getId())));
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
        Order order = orderRepository.findOne(orderId);
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

}
