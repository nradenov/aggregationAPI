package com.aggregationservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;
import java.util.Set;

public class IsoCodeValidator implements ConstraintValidator<IsoCode2, String> {

    private static final Set<String> countryCodes = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2);
    @Override
    public boolean isValid(String countryCode, ConstraintValidatorContext constraintValidatorContext) {
        return countryCodes.contains(countryCode);
    }
}
