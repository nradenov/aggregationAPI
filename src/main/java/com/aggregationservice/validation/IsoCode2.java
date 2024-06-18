package com.aggregationservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target( { FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsoCodeValidator.class)
public @interface IsoCode2 {
    String message() default "Not valid ISO2 country code ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
