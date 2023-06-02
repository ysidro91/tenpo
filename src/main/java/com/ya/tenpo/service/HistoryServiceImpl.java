package com.ya.tenpo.service;

import com.ya.tenpo.model.History;
import com.ya.tenpo.repository.HistoryRepository;
import com.ya.tenpo.util.CallType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class HistoryServiceImpl implements HistoryService {

    private static final Logger log = LogManager.getLogger(HistoryServiceImpl.class);

    @Autowired
    HistoryRepository historyRepository;

    @Override
    public Mono<Page<History>> getAll(PageRequest pageRequest) {
        return this.historyRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(this.historyRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()))
                .doOnNext( page -> {
                    String requestInfo = new StringBuilder("{number: ")
                            .append(page.getNumber())
                            .append(", size: ")
                            .append(page.getSize())
                            .append("}")
                            .toString();
                    String responseInfo = new StringBuilder("{numberOfElements: ")
                            .append(page.getNumberOfElements())
                            .append("}")
                            .toString();
                    historyRepository.save(new History(CallType.HISTORY, requestInfo, responseInfo))
                            .publishOn(Schedulers.boundedElastic())
                            .doOnNext(entity -> log.info("Saved entity: {}", entity.toString()))
                            .doOnError(exception -> log.error("Error: {}", exception.getMessage()))
                            .subscribe();
                })
                .map(page -> page);
    }

}
