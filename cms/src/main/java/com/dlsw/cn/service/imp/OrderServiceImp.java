package com.dlsw.cn.service.imp;

import com.dlsw.cn.dto.OrderDTO;
import com.dlsw.cn.mapper.OrderMapper;
import com.dlsw.cn.po.Order;
import com.dlsw.cn.repositories.OrderRepository;
import com.dlsw.cn.service.BaseService;
import com.dlsw.cn.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public OrderDTO getOrder(long orderId) {
        Order order = orderRepository.findOneById(orderId);
        return orderMapper.orderToOrderDTO(order);
    }
}
