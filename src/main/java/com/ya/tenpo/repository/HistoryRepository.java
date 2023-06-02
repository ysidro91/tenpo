package com.ya.tenpo.repository;

import com.ya.tenpo.model.History;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface HistoryRepository extends ReactiveCrudRepository<History, Long> {

    Flux<History> findAllBy(Pageable pageable);

}
