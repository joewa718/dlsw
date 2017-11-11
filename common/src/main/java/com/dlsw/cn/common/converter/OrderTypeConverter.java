package com.dlsw.cn.common.converter;


import com.dlsw.cn.common.enumerate.OrderType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderTypeConverter implements AttributeConverter<OrderType, Integer> {
    public Integer convertToDatabaseColumn(OrderType value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public OrderType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return OrderType.fromCode(value);
    }
}