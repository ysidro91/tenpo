package com.ya.tenpo.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.ya.tenpo.exception.GeneralClientException;
import com.ya.tenpo.exception.ServiceUnavaliableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RandomClientTest {

    @Spy
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Mock
    Cache<String, String> cache;

    @InjectMocks
    RandomClient randomClient;

    @Test
    public void givenServiceReturnOk() {
        String percentage = "30";

        when(cache.getIfPresent(anyString()))
                .thenReturn(null);
        when(webClient.get())
                .thenReturn(uriSpec);
        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);
        when(headersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just(percentage));

        StepVerifier.create(randomClient.fetchData())
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    public void givenResponseFromCacheReturnOk() {
        String percentage = "50";

        when(cache.getIfPresent(anyString()))
                .thenReturn(percentage);

        StepVerifier.create(randomClient.fetchData())
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    public void givenUnknownErrorReturnException() {
        when(cache.getIfPresent(anyString()))
                .thenReturn(null);
        when(webClient.get())
                .thenReturn(uriSpec);
        when(uriSpec.uri(anyString()))
                .thenThrow(new NullPointerException());

        StepVerifier.create(randomClient.fetchData())
                .consumeErrorWith(error -> {
                    Assertions.assertInstanceOf(GeneralClientException.class, error);
                })
                .verify();
    }

    @Test
    public void givenWebErrorReturnResponseStatusException() {
        when(cache.getIfPresent(anyString()))
                .thenReturn(null);
        when(webClient.get())
                .thenReturn(uriSpec);
        when(uriSpec.uri(anyString()))
                .thenReturn(headersSpec);
        when(headersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(new WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal error", null, null, null)));

        StepVerifier.create(randomClient.fetchData())
                .consumeErrorWith(error -> {
                    Assertions.assertInstanceOf(ServiceUnavaliableException.class, error);
                    Assertions.assertEquals(error.getMessage(), "Retries exhausted. Last message: 500 Internal error");
                })
                .verify();
    }
}
