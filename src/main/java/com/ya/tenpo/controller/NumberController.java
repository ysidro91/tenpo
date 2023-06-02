package com.ya.tenpo.controller;

import com.ya.tenpo.dto.CalculateRequest;
import com.ya.tenpo.dto.CalculateResponse;
import com.ya.tenpo.exception.RequestLimitException;
import com.ya.tenpo.service.NumberService;
import io.github.bucket4j.Bucket;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/numbers")
public class NumberController {

    @Autowired
    Bucket bucket;

    @Autowired
    NumberService calculateService;

    @PostMapping("/calculate")
    public Mono<CalculateResponse> calculate(@Valid @RequestBody CalculateRequest request) {
        if (bucket.tryConsume(1)) {
            return calculateService.calculate(request);
        }

        return Mono.error(new RequestLimitException());
    }

}
