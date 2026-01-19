package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;
import com.mk.workflow_engine.implementation.OrderRequest;
import com.mk.workflow_engine.implementation.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;

@Component
public class OrderPipelineNodes  {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    public OrderPipelineNodes(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public DeliveryTypeNode deliveryTypeNode() {
        return new DeliveryTypeNode(orderService);
    }
    @Bean
    public OrderPipelineStart orderPipelineStart() {
        return new OrderPipelineStart();
    }
    @Bean
    public PaymentSuccessNode paymentSuccessNode() {
        return new PaymentSuccessNode(orderService);
    }
    @Bean
    public PaymentFailedNode paymentFailedNode() {
        return new PaymentFailedNode();
    }
    @Bean
    public OrderPipelineCreate orderPipelineCreate() {
        return new OrderPipelineCreate(orderService,objectMapper);
    }
    @Bean
    public OrderPipelineEnd orderPipelineEnd() {
        return new OrderPipelineEnd();
    }
}
