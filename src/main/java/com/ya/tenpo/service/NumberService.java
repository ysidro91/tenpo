package com.ya.tenpo.service;

import com.ya.tenpo.dto.CalculateRequest;
import com.ya.tenpo.dto.CalculateResponse;
import reactor.core.publisher.Mono;

public interface NumberService {

     Mono<CalculateResponse> calculate(CalculateRequest request);

}
