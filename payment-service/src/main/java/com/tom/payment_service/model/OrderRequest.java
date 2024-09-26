package com.tom.payment_service.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequest {

    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String totalAmount;
    private String extOrderId;
    private Buyer buyer;
    private List<Product> products;
}
