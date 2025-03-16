package com.tom.payment_service.model.dto;

import com.tom.payment_service.model.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String id;
    private String orderNumber;
    private String paymentServiceNumber;
    private LocalDateTime createDate;
    private Long studentId;
    private String courseId;
    private PaymentStatus status;
    private String totalAmount;

}