package com.dlsw.cn.web.converter;

import com.dlsw.cn.web.enumerate.RebateStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RebateStatusConverter implements AttributeConverter<RebateStatus, Integer> {
    public Integer convertToDatabaseColumn(RebateStatus value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public RebateStatus convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return RebateStatus.fromCode(value);
    }
}