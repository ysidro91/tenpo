package com.ya.tenpo.dto;

import jakarta.validation.constraints.NotNull;

public class CalculateRequest {

    @NotNull(message = "X is required.")
    private Double x;
    @NotNull(message = "Y is required.")
    private Double y;

    public CalculateRequest(@NotNull(message = "X is required.") Double x, @NotNull(message = "Y is required.") Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" +
                "x: " + x +
                ", y: " + y +
                '}';
    }
}
