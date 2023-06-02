package com.ya.tenpo.service;

import com.ya.tenpo.model.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

public interface HistoryService {

    Mono<Page<History>> getAll(PageRequest pageRequest);

}
