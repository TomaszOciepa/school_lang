package com.tom.payment_service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderResponse {
    private String status;
    private String redirectUri;
    private String orderId;
    private String extOrderId;
}
