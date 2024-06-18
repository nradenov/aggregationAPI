package com.aggregationservice.integration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Data
@Slf4j
public class RemoteApiClient<T> {
    private String urlPattern;

    public <T> Mono<T> get(Map<String, String> params, Class<T> clazz){
        return  WebClient.builder().build().get().uri(applyPattern(urlPattern, params)).retrieve().bodyToMono(clazz)
            .timeout(Duration.ofSeconds(5), Mono.empty())
            //.log()
            .onErrorResume(e -> Mono.empty());
    }

    private String applyPattern(String urlPattern, Map<String, String> params){
        AtomicReference<String> result = new AtomicReference<>(urlPattern);
        for(String key : params.keySet()){
            result.set(result.get().replace(":"+key, params.get(key)));
        }
        return result.get();
    }


    public RemoteApiClient<T> withUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
        return this;
    }

}
