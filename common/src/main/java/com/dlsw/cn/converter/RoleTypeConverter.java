package com.dlsw.cn.converter;

import com.dlsw.cn.enumerate.RoleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RoleTypeConverter implements AttributeConverter<RoleType, Integer> {

    public Integer convertToDatabaseColumn(RoleType value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public RoleType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return RoleType.fromCode(value);
    }
}