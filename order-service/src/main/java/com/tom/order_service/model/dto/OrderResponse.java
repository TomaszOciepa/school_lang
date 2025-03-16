package com.tom.order_service.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderResponse {
    private StatusResponse status;
    private String redirectUri;
    private String orderId;
    private String extOrderId;
}
