package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.web.dto.OrderDTO;
import com.dlsw.cn.web.enumerate.*;
import com.dlsw.cn.web.po.*;
import com.dlsw.cn.web.repositories.*;
import com.dlsw.cn.web.service.BaseService;
import com.dlsw.cn.web.service.PromoteService;
import com.dlsw.cn.web.service.RebateService;
import com.dlsw.cn.web.util.DateUtil;
import com.dlsw.cn.web.util.GenerateRandomCode;
import com.dlsw.cn.web.mapper.OrderMapper;
import com.dlsw.cn.web.service.OrderCheckService;
import com.dlsw.cn.web.service.OrderService;
import com.dlsw.cn.web.vo.OrderVo;
import com.dlsw.cn.web.vo.PayCertificateVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private OrderCheckService orderCheckService;
    @Autowired
    private RebateService rebateService;
    @Autowired
    private PromoteService promoteService;

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

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO applyOrder(String phone, OrderVo orderVo) {
        User user = userRepository.findByPhone(phone);
        Product product = productRepository.findOne(orderVo.getProductId());
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIdAndUser(orderVo.getDeliverAddressId(), user);
        //向上查找比自己级别高的用户
        User recommend_man = userRepository.findByPhone(orderVo.getRecommendPhone());
        orderCheckService.applyOrder(user, product, recommend_man, deliveryAddress, orderVo);
        if (!orderCheckService.gtLevelSelf(user, recommend_man)) {
            while (recommend_man == null) {
                if (orderCheckService.gtLevelSelf(user, recommend_man)) {
                    break;
                }
                recommend_man = recommend_man.getHigher();
            }
        }
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
    public OrderDTO sureOrder(String phone, long orderId) {
        User user = userRepository.findByPhone(phone);
        Order order = orderRepository.findOne(orderId);
        User orderUser = order.getUser();
        orderCheckService.sureOrder(order, user, orderUser);
        order.setOrderStatus(OrderStatus.已支付);
        orderRepository.save(order);
        Product product = productRepository.getProductByproductCode(order.getProductCode());
        if (product.getProductType() == ProductType.套餐产品 && product.getRoleType().getCode() > user.getRoleType().getCode()) {
            if (user.getAuthorizationCode() == null) {
                user.setAuthorizationCode(GenerateRandomCode.generateAuthCode());
            }
            user.setRoleType(product.getRoleType());
            if(user.getRoleType() == RoleType.VIP){
                promoteService.monitorVip(order);
            }else if(user.getRoleType() == RoleType.合伙人){
                promoteService.monitorPartner(order);
            }
        }
        if (orderUser.getHigher() == null && orderUser.getRoleType().getCode() > RoleType.普通.getCode()) {
            List<User> grandUserList = userRepository.findByLikeOrgPath(getEqualStr(orderUser));
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
        rebateService.calRebate(order);
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
