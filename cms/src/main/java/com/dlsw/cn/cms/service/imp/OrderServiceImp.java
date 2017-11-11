package com.dlsw.cn.cms.service.imp;

import com.dlsw.cn.cms.service.OrderService;
import com.dlsw.cn.cms.service.PageService;
import com.dlsw.cn.common.dto.OrderDTO;
import com.dlsw.cn.common.dto.PageDTO;
import com.dlsw.cn.common.dto.RebateDTO;
import com.dlsw.cn.common.enumerate.OrderStatus;
import com.dlsw.cn.common.enumerate.ProductType;
import com.dlsw.cn.common.enumerate.RebateStatus;
import com.dlsw.cn.common.enumerate.RoleType;
import com.dlsw.cn.cms.mapper.OrderMapper;
import com.dlsw.cn.common.po.Order;
import com.dlsw.cn.common.po.Product;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.common.repositories.OrderRepository;
import com.dlsw.cn.common.repositories.ProductRepository;
import com.dlsw.cn.common.repositories.UserRepository;
import com.dlsw.cn.common.service.PromoteService;
import com.dlsw.cn.common.service.RewardStrategyService;
import com.dlsw.cn.common.util.GenerateRandomCode;
import com.dlsw.cn.cms.vo.OrderVo;
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
    @Autowired
    private PromoteService promoteService;
    @Autowired
    private RewardStrategyService rewardStrategyService;

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
            if(orderUser.getRoleType() == RoleType.VIP){
                promoteService.monitorVip(order);
            }else if(orderUser.getRoleType() == RoleType.合伙人){
                promoteService.monitorPartner(order);
            }
        }

        User topUser = userRepository.findByPhone("18930983718");
        if (orderUser.getHigher() == null && orderUser.getRoleType().getCode() > RoleType.普通.getCode()) {
            List<User> grandUserList = userRepository.findByLikeOrgPath(getOrgPath(orderUser));
            if (grandUserList != null && grandUserList.size() > 0) {
                grandUserList.forEach(lower -> {
                    lower.setOrgPath(bindOffSpringOrgPath(topUser, lower));
                    userRepository.save(lower);
                });
            }
            topUser.getLower().add(orderUser);
            orderUser.setHigher(topUser);
            orderUser.setLevel(topUser.getLevel() + 1);
            orderUser.setOrgPath(bindOffSpringOrgPath(topUser, orderUser));
            userRepository.save(orderUser);
        }
        rewardStrategyService.calRebate(order);
        return orderMapper.orderToOrderDTO(order);
    }
}
