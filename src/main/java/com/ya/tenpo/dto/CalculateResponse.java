package com.ya.tenpo.dto;

public class CalculateResponse {

    private Double result;

    public CalculateResponse(Double result) {
        this.result = result;
    }

    public Double getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "result: " + result +
                '}';
    }
}
