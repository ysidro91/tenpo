package com.ya.tenpo.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ya.tenpo.exception.GeneralClientException;
import com.ya.tenpo.exception.ServiceUnavaliableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RandomClient {

    private static final Logger log = LogManager.getLogger(RandomClient.class);

    @Value("${number.uri}")
    private String uri;

    @Value("${webclient.max.attempts}")
    private long webclientMaxAttempts;

    @Value("${webclient.seconds.delay}")
    private long webclientSecondsDelay;

    private final String params = "num=1&min=1&max=100&col=1&base=10&format=plain";

    @Autowired
    private WebClient webClient;

    @Autowired
    private Cache<String, String> cache;

    public Mono<String> fetchData() {
        try {
            String requestUri = String.format("%s?%s", uri, params);

            String cacheItem = cache.getIfPresent(requestUri);
            if (cacheItem != null) {
                return Mono.just(cacheItem);
            }

            return webClient.get()
                    .uri(requestUri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> cache.put(requestUri, response))
                    .retryWhen(Retry.fixedDelay(webclientMaxAttempts, Duration.ofSeconds(webclientSecondsDelay))
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                                String message = String.format("Retries exhausted. Last message: %s",
                                        retrySignal.failure().getMessage());
                                throw new ServiceUnavaliableException(message );
                            })
                    )
                    .onErrorResume(WebClientResponseException.class, error -> {
                        log.error("Error status: {}. Message {}", error.getStatusCode(), error.getMessage());
                        return Mono.error(new ResponseStatusException(error.getStatusCode(), error.getMessage(), error));
                    });
        } catch (Exception ex) {
            log.error("Internal error message {}", ex.getMessage());
            return Mono.error(new GeneralClientException(ex));
        }
    }
}
