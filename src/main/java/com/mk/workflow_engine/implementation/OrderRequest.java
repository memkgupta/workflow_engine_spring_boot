package com.mk.workflow_engine.implementation;

import lombok.Data;

@Data
public class OrderRequest {

    private String customerName;
    private Double amount;
    private String deliveryType;
    private Boolean paymentSuccess;

    // getters & setters
}
