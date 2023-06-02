package com.ya.tenpo.controller;

import com.ya.tenpo.exception.RequestLimitException;
import com.ya.tenpo.model.History;
import com.ya.tenpo.service.HistoryService;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    Bucket bucket;

    @Autowired
    HistoryService historyService;

    @GetMapping
    public Mono<Page<History>> getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        if (bucket.tryConsume(1)) {
            return historyService.getAll(PageRequest.of(page, size));
        }

        return Mono.error(new RequestLimitException());
    }

}
