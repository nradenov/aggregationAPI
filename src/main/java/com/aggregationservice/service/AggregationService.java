package com.aggregationservice.service;

import com.aggregationservice.integration.RemoteApiClient;
import com.aggregationservice.model.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AggregationService {

    private final RemoteApiClient<List<Product>> shipmentsClient;
    private final RemoteApiClient<Tracking> trackingClient;
    private final RemoteApiClient<Pricing> pricingClient;

    public AggregationService(RemoteApiClient<List<Product>> shipmentsClient,
                              RemoteApiClient<Tracking> trackingClient,
                              RemoteApiClient<Pricing> pricingClient){
        this.shipmentsClient = shipmentsClient;
        this.trackingClient = trackingClient;
        this.pricingClient = pricingClient;
    }

    public Mono<Aggregate> aggregate(List<OrderNumber> shipmentONs, List<OrderNumber> trackingONs, List<CountryCode> pricingCCs){

        var schipmentMonoMap = Optional.ofNullable(shipmentONs).map(ons -> Flux.fromStream(ons.stream().distinct())
            .flatMap(this::getShipment)
            .collectMap(Shipment::orderNumber, Shipment::products)).orElse(Mono.just(Map.of()));

        var trackingMonoMap = Optional.ofNullable(trackingONs).map(ons -> Flux.fromStream(ons.stream().distinct())
            .flatMap(this::getTracking)
            .collectMap(Tracking::orderNumber, Tracking::trackingStatus)).orElse(Mono.just(Map.of()));

        var pricingMonoMap = Optional.ofNullable(pricingCCs).map(ccs -> Flux.fromStream(ccs.stream().distinct())
            .flatMap(this::getPricing)
            .collectMap(Pricing::countryCode, Pricing::price)).orElse(Mono.just(Map.of()));

        return Mono.zip(schipmentMonoMap, trackingMonoMap, pricingMonoMap)
            .map(this::combine);

    }

    private Aggregate combine(Tuple3<Map<OrderNumber, List<Product>>, Map<OrderNumber, TrackingStatus>, Map<CountryCode, Double>> tuple) {
        return new Aggregate(tuple.getT1(), tuple.getT2(), tuple.getT3());
    }

    public Mono<Shipment> getShipment(final OrderNumber orderNumber) {
        return shipmentsClient.get(Map.of("orderNumber", orderNumber.getValue()), List.class).map(list -> list.stream().map(li -> Product.valueOf((String)li)).toList()).map(products -> new Shipment(orderNumber, products));
    }

    public Mono<Tracking> getTracking(OrderNumber orderNumber) {
        return trackingClient.get(Map.of("orderNumber", orderNumber.getValue()), TrackingStatus.class).map(item -> new Tracking(orderNumber, item));
    }

    public Mono<Pricing> getPricing(CountryCode countryCode) {
        return pricingClient.get(Map.of("countryCode", countryCode.getValue()), Double.class).map(item -> new Pricing(countryCode, item));
    }
}
