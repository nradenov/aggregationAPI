package com.aggregationservice.model;

import com.aggregationservice.validation.IsoCode2;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryCode {
    @Pattern(regexp = "[A-Z]{2}", message = "Must have exactly 2 uppercase characters")
    @IsoCode2
    @JsonValue
    private String value;
}
