package com.tom.payment_service.model;

import com.tom.payment_service.model.payu.StatusResponse;
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
