package com.tom.order_service.model.dto;

import lombok.ToString;

@ToString
public class StatusResponse {
    private String statusCode;

    // Gettery i settery
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}

