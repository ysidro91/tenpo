package com.ya.tenpo.service;

import com.ya.tenpo.client.RandomClient;
import com.ya.tenpo.dto.CalculateRequest;
import com.ya.tenpo.dto.CalculateResponse;
import com.ya.tenpo.model.History;
import com.ya.tenpo.repository.HistoryRepository;
import com.ya.tenpo.util.CallType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class NumberServiceImpl implements NumberService {

    private static final Logger log = LogManager.getLogger(NumberServiceImpl.class);

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    RandomClient randomClient;

    @Override
    public Mono<CalculateResponse> calculate(CalculateRequest request) {
        double plus = request.getX() + request.getY();
        return randomClient.fetchData().flatMap(response -> {
            double result = (Double.parseDouble(response) * plus) / 100;
            return Mono.just(new CalculateResponse(result + plus));
        }).doOnNext(response -> {
            historyRepository.save(new History(CallType.CALCULATE, request.toString(), response.toString()))
                    .publishOn(Schedulers.boundedElastic())
                    .doOnNext(entity -> log.info("Saved entity: {}", entity.toString()))
                    .doOnError(exception -> log.error("Error: {}", exception.getMessage()))
                    .subscribe();
        }).map(response -> response);
    }

}
