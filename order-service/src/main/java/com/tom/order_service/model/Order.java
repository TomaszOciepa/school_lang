package com.tom.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Order {

    @Id
    private String id;
    private String orderNumber;
    private String paymentServiceNumber;
    private LocalDateTime createDate;
    private Long studentId;
    private String courseId;
    private Status status;
    private String totalAmount;

    public Order() {
    }
}



