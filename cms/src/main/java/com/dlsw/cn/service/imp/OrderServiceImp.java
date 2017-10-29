package com.dlsw.cn.service.imp;

import com.dlsw.cn.dto.OrderDTO;
import com.dlsw.cn.dto.PageDTO;
import com.dlsw.cn.dto.RebateDTO;
import com.dlsw.cn.enumerate.OrderStatus;
import com.dlsw.cn.enumerate.ProductType;
import com.dlsw.cn.enumerate.RebateStatus;
import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.mapper.OrderMapper;
import com.dlsw.cn.po.Order;
import com.dlsw.cn.po.Product;
import com.dlsw.cn.po.User;
import com.dlsw.cn.repositories.OrderRepository;
import com.dlsw.cn.repositories.ProductRepository;
import com.dlsw.cn.service.BaseService;
import com.dlsw.cn.service.OrderService;
import com.dlsw.cn.util.GenerateRandomCode;
import com.dlsw.cn.vo.OrderVo;
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
import java.util.stream.Collectors;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class OrderServiceImp extends BaseService implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);

    @Autowired
    private OrderRepository orderRepository;
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
        return orderMapper.orderToOrderDTO(order);
    }
}
