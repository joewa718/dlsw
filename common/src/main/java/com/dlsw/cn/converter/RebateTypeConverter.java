package com.dlsw.cn.converter;

import com.dlsw.cn.enumerate.RebateType;
import com.dlsw.cn.enumerate.RoleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RebateTypeConverter implements AttributeConverter<RebateType, Integer> {

    public Integer convertToDatabaseColumn(RebateType value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public RebateType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return RebateType.fromCode(value);
    }
}