package com.aggregationservice.model;

import java.util.List;

public record Shipment(OrderNumber orderNumber, List<Product> products) {
}
