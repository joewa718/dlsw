package com.dlsw.cn.web.mapper;

import com.dlsw.cn.converter.ArrayMapper;
import com.dlsw.cn.web.dto.OrderDTO;
import com.dlsw.cn.po.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses=ArrayMapper.class)
public interface OrderMapper {
    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> orderToOrderDTOList(List<Order> orderList);

    List<OrderDTO> orderToOrderDTOList(Iterable<Order> orderList);
}