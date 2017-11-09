package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.web.dto.OrderDTO;
import com.dlsw.cn.web.dto.PageDTO;
import com.dlsw.cn.web.dto.RebateDTO;
import com.dlsw.cn.web.enumerate.OrderStatus;
import com.dlsw.cn.web.enumerate.ProductType;
import com.dlsw.cn.web.enumerate.RebateStatus;
import com.dlsw.cn.web.mapper.OrderMapper;
import com.dlsw.cn.web.po.Order;
import com.dlsw.cn.web.po.Product;
import com.dlsw.cn.web.po.User;
import com.dlsw.cn.web.repositories.OrderRepository;
import com.dlsw.cn.web.repositories.ProductRepository;
import com.dlsw.cn.web.repositories.UserRepository;
import com.dlsw.cn.web.service.OrderService;
import com.dlsw.cn.web.service.PageService;
import com.dlsw.cn.web.util.GenerateRandomCode;
import com.dlsw.cn.web.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class OrderServiceImp extends PageService implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public PageDTO<RebateDTO> fetchPage(OrderVo orderVo) {
        Page page = orderRepository.findAll((root, cq, cb) -> {
            List<Predicate> list = new ArrayList();
            if (!StringUtils.isBlank(orderVo.getOrderCode())) {
                list.add(cb.equal(root.get("orderCode").as(String.class), orderVo.getOrderCode()));
            }
            if (orderVo.getOrderStatus() != null) {
                list.add(cb.equal(root.get("orderStatus").as(RebateStatus.class), orderVo.getOrderStatus()));
            }
            if (orderVo.getUser() != null) {
                list.add(cb.equal(root.get("user").as(User.class), orderVo.getUser()));
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        }, super.getPageRequest(orderVo));

        PageDTO<RebateDTO> rebateDTOPageDTO = new PageDTO<>();
        rebateDTOPageDTO.setTotalElements(page.getTotalElements());
        rebateDTOPageDTO.setContent(orderMapper.orderToOrderDTOList(page.getContent()));
        return rebateDTOPageDTO;
    }

    @Override
    public OrderDTO getOrder(long orderId) {
        Order order = orderRepository.findOne(orderId);
        return orderMapper.orderToOrderDTO(order);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO sureOrder(long orderId) {
        Order order = orderRepository.findOne(orderId);
        User orderUser = order.getUser();
        //修改订单状态 为已支付
        order.setOrderStatus(OrderStatus.已支付);
        orderRepository.save(order);
        //更新订单人级别,如果是套餐 并且 套餐的产品级别大于用户级别 ，修改用户级别
        Product product = productRepository.getProductByproductCode(order.getProductCode());
        if (product.getProductType() == ProductType.套餐产品 && product.getRoleType().getCode() > orderUser.getRoleType().getCode()) {
            if (orderUser.getAuthorizationCode() == null) {
                orderUser.setAuthorizationCode(GenerateRandomCode.generateAuthCode());
            }
            orderUser.setRoleType(product.getRoleType());
        }
        /*if (orderUser.getHigher() == null && orderUser.getRoleType().getCode() > RoleType.普通.getCode()) {
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
        rebateService.calRebate(order);*/
        return orderMapper.orderToOrderDTO(order);
    }
}
