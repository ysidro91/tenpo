package com.ya.tenpo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${number.base.url}")
    private String baseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)
                ))
                .build();
    }

}
