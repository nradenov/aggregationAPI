package com.aggregationservice.conversion;

import com.aggregationservice.model.OrderNumber;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToOrderNumberConverter implements Converter<String, OrderNumber> {
    @Override
    public OrderNumber convert(String value) {
        return OrderNumber.builder().value(value).build();
    }
}
