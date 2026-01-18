package com.mk.workflow_engine.implementation;



import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order create(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setAmount(request.getAmount());
        order.setDeliveryType(request.getDeliveryType());
        order.setPaymentSuccess(request.getPaymentSuccess());
        return orderRepository.save(order);
    }

    public Order get(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
