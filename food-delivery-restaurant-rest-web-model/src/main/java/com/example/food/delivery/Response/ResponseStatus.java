package com.example.food.delivery.Response;

public enum ResponseStatus {
    SUCCESS("OK"),
    ERROR("Error");

    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
