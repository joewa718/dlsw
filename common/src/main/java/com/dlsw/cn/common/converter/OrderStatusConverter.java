package com.dlsw.cn.common.converter;

import com.dlsw.cn.common.enumerate.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {

    public Integer convertToDatabaseColumn(OrderStatus value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public OrderStatus convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return OrderStatus.fromCode(value);
    }
}