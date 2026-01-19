package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;
import com.mk.workflow_engine.implementation.Order;
import com.mk.workflow_engine.implementation.OrderRequest;
import com.mk.workflow_engine.implementation.OrderService;
import tools.jackson.databind.ObjectMapper;

@Node(
        type = "task",
        id = "CREATE_ORDER",
        isRoot = false,
        workflowName = "ORDER_PIPELINE"
)
public class OrderPipelineCreate extends WorkflowNode {
    public OrderPipelineCreate(OrderService orderService , ObjectMapper objectMapper) {

        super.task= (ctx)->{
            OrderRequest
                    orderRequest = objectMapper.convertValue(
ctx.getAttributes().get("orderRequest"),OrderRequest.class
            );
            Order order = orderService.create(orderRequest);
            ctx.getAttributes().put("orderId",order.getId());
        };

        super.conditionalMove= (ctx)->{
            return ctx.getAttributes().get("orderId") != null;
        };

    }
}
