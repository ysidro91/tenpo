package com.ya.tenpo.model;

import com.ya.tenpo.util.CallType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

public class History {

    @Id
    private long id;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("type")
    private CallType type;

    @Column("request")
    private String request;

    @Column("response")
    private String response;

    public History(CallType type, String request, String response) {
        this.type = type;
        this.request = request;
        this.response = response;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CallType getType() {
        return type;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "{" +
                "type: " + type +
                ", request:'" + request + '\'' +
                ", response: '" + response + '\'' +
                '}';
    }
}
