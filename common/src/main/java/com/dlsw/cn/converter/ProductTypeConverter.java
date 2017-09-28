package com.dlsw.cn.converter;

import com.dlsw.cn.enumerate.ProductType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProductTypeConverter implements AttributeConverter<ProductType, Integer> {
    public Integer convertToDatabaseColumn(ProductType value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public ProductType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return ProductType.fromCode(value);
    }
}