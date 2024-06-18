package com.aggregationservice.rest;

import com.aggregationservice.integration.RemoteApiClient;
import com.aggregationservice.model.Pricing;
import com.aggregationservice.model.Product;
import com.aggregationservice.model.Tracking;
import com.aggregationservice.rest.error.GlobalErrorAttributes;
import com.aggregationservice.service.AggregationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AggregationController.class)
@Import({AggregationService.class,  GlobalErrorAttributes.class})
class AggregationControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    RemoteApiClient<List<Product>> shipmentClient;

    @MockBean
    RemoteApiClient<Tracking> trackingClient;

    @MockBean
    RemoteApiClient<Pricing> pricingClient;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenWrongOrderIdWhenFetchOrderThenBadRequest() {

        Mockito.when(shipmentClient.get(Map.of("orderNumber", "123456789"), List.class)).thenReturn(Mono.just(List.of(Product.PALLET.name())));

        webClient.get().uri("/aggregation?shipmentsOrderNumber=1234567890")
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isNotEmpty()
            .jsonPath("$.message").isEqualTo("fetchOrder.shipmentOrderNumbers[0].value: This field should contain 9 digits!")
            .consumeWith(response -> System.out.println(new String(response.getResponseBody())));
    }

    @Test
    void givenCorrectOrderIdWhenFetchOrderThenShipmentPresent() {

        Mockito.when(shipmentClient.get(Map.of("orderNumber", "123456789"), List.class)).thenReturn(Mono.just(List.of(Product.PALLET.name())));

        webClient.get().uri("/aggregation?shipmentsOrderNumber=123456789")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.message").doesNotExist()
            .jsonPath("$.shipments").isNotEmpty()
            .consumeWith(response -> System.out.println(new String(response.getResponseBody())));
    }

    @Test
    void givenCorrectCountryCodeWhenFetchOrderThenShipmentAndPricingPresent() {

        Mockito.when(shipmentClient.get(Map.of("orderNumber", "123456789"), List.class)).thenReturn(Mono.just(List.of(Product.PALLET.name())));
        Mockito.when(pricingClient.get(Map.of("countryCode", "NL"), Double.class)).thenReturn(Mono.just(22.0));
        Mockito.when(pricingClient.get(Map.of("countryCode", "DE"), Double.class)).thenReturn(Mono.just(33.0));

        webClient.get().uri("/aggregation?shipmentsOrderNumber=123456789&pricingCountryCodes=NL,DE")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.message").doesNotExist()
            .jsonPath("$.shipments").isNotEmpty()
            .jsonPath("$.pricing").isNotEmpty()
            .consumeWith(response -> System.out.println(new String(response.getResponseBody())));
    }

    @Test
    void givenInorrectCountryCodeWhenFetchOrderThenBadRequest() {

        Mockito.when(shipmentClient.get(Map.of("orderNumber", "123456789"), List.class)).thenReturn(Mono.just(List.of(Product.PALLET.name())));
        Mockito.when(pricingClient.get(Map.of("countryCode", "NL"), Double.class)).thenReturn(Mono.just(22.0));
        Mockito.when(pricingClient.get(Map.of("countryCode", "DE"), Double.class)).thenReturn(Mono.just(33.0));

        webClient.get().uri("/aggregation?shipmentsOrderNumber=123456789&pricingCountryCodes=NL,DEX")
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isNotEmpty()
            .jsonPath("$.shipments").doesNotExist()
            .jsonPath("$.pricing").doesNotExist()
            .consumeWith(response -> System.out.println(new String(response.getResponseBody())));
    }
}


