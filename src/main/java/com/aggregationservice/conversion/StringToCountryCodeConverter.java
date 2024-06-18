package com.aggregationservice.conversion;

import com.aggregationservice.model.CountryCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCountryCodeConverter implements Converter<String, CountryCode> {
    @Override
    public CountryCode convert(String value) {
        return CountryCode.builder().value(value).build();
    }
}
