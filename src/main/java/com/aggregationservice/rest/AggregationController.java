package com.aggregationservice.rest;

import com.aggregationservice.model.Aggregate;
import com.aggregationservice.model.CountryCode;
import com.aggregationservice.model.OrderNumber;
import com.aggregationservice.service.AggregationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("aggregation")
@Validated
public class AggregationController {

    private final AggregationService aggregationService;

    public AggregationController(final AggregationService aggregationService){
        this.aggregationService = aggregationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Aggregate> fetchOrder(@Valid @RequestParam(value = "shipmentsOrderNumber", required = false) List<OrderNumber> shipmentOrderNumbers,
                                                      @Valid @RequestParam(value = "trackOrderNumber", required = false) List<OrderNumber> trackOrderNumbers,
                                                      @Valid @RequestParam(value = "pricingCountryCodes", required = false) List<CountryCode> pricingCountryCodes){
        return aggregationService.aggregate(shipmentOrderNumbers, trackOrderNumbers, pricingCountryCodes);
    }
}
