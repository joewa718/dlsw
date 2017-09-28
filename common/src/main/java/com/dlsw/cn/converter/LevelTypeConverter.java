package com.dlsw.cn.converter;


import com.dlsw.cn.enumerate.LevelType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LevelTypeConverter implements AttributeConverter<LevelType, Integer> {
    public Integer convertToDatabaseColumn(LevelType value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public LevelType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return LevelType.fromCode(value);
    }
}