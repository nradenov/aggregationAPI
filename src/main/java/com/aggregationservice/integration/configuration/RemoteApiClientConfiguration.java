package com.aggregationservice.integration.configuration;

import com.aggregationservice.integration.RemoteApiClient;
import com.aggregationservice.model.Pricing;
import com.aggregationservice.model.Product;
import com.aggregationservice.model.Tracking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RemoteApiClientConfiguration {

    @Value("${remoteApiClient.shipment.url}")
    private String shipmentUrl;

    @Value("${remoteApiClient.tracking.url}")
    private String trackingUrl;

    @Value("${remoteApiClient.pricing.url}")
    private String pricingUrl;

    @Bean(name = "shipmentClient")
    public RemoteApiClient<List<Product>> shipmentClient() {
        return new RemoteApiClient<List<Product>>().withUrlPattern(shipmentUrl);
    }

    @Bean(name = "trackingClient")
    public RemoteApiClient<Tracking> trackingClient() {
        return new RemoteApiClient<Tracking>().withUrlPattern(trackingUrl);
    }

    @Bean(name = "pricingClient")
    public RemoteApiClient<Pricing> pricingClient() {
        return new RemoteApiClient<Pricing>().withUrlPattern(pricingUrl);
    }
}
