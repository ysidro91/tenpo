package com.ya.tenpo.dto;

import org.springframework.data.relational.core.mapping.Column;

public class HistoryResponse {

    private long id;
    private String request;
    private String response;

    public HistoryResponse(long id, String request, String response) {
        this.id = id;
        this.request = request;
        this.response = response;
    }
}
