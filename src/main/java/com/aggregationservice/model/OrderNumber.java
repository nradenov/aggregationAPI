package com.aggregationservice.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderNumber {

    @Pattern(regexp="\\d{9}", message="This field should contain 9 digits!")
    @JsonValue
    private String value;
}
