package com.aggregationservice.model;

import java.util.List;
import java.util.Map;

public record Aggregate(Map<OrderNumber, List<Product>> shipments,
                        Map<OrderNumber, TrackingStatus> track,
                        Map<CountryCode, Double> pricing) {
}
