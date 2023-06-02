package com.ya.tenpo.service;

import com.ya.tenpo.client.RandomClient;
import com.ya.tenpo.dto.CalculateRequest;
import com.ya.tenpo.model.History;
import com.ya.tenpo.repository.HistoryRepository;
import com.ya.tenpo.util.CallType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NumberServiceTest {

    @Mock
    RandomClient randomClient;

    @Mock
    HistoryRepository historyRepository;

    @InjectMocks
    NumberServiceImpl numberService;

    @Test
    public void givenServiceReturnOk() {
        CalculateRequest request = new CalculateRequest(7d, 9d);

        when(historyRepository.save(any())).thenReturn(Mono.just(new History(CallType.CALCULATE,"", "")));
        when(randomClient.fetchData()).thenReturn(Mono.just("10"));

        StepVerifier.create(numberService.calculate(request))
                .consumeNextWith(response -> {
                    Assertions.assertEquals(response.getResult(), 17.6);
                })
                .verifyComplete();
    }

}
