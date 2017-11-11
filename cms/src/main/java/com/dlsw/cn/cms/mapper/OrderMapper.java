package com.dlsw.cn.cms.mapper;

import com.dlsw.cn.common.converter.ArrayMapper;
import com.dlsw.cn.common.dto.OrderDTO;
import com.dlsw.cn.common.po.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses=ArrayMapper.class)
public interface OrderMapper {
    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> orderToOrderDTOList(List<Order> orderList);

    List<OrderDTO> orderToOrderDTOList(Iterable<Order> orderList);
}