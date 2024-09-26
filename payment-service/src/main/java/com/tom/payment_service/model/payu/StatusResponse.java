package com.tom.payment_service.model.payu;

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
