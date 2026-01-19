package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.CaseNode;
import com.mk.workflow_engine.annotations.Node;
import com.mk.workflow_engine.implementation.Order;
import com.mk.workflow_engine.implementation.OrderService;


import java.util.UUID;
import java.util.stream.Stream;

@Node(
        type = "case",
        id = "DELIVERY_TYPE",
        isRoot = true,
        workflowName = "ORDER_PIPELINE"
)
public class DeliveryTypeNode extends CaseNode<String> {
    public DeliveryTypeNode(OrderService orderService) {
        super(
                (ctx)->{
                    Order order = orderService.get(
                            (UUID)ctx.getAttributes().get("orderId")
                    );
                    ctx.getAttributes().put("deliveryType", order.getDeliveryType());
                    return order.getDeliveryType();
                }
        );
        super.conditionalMove = (ctx)-> Stream.of("EXPRESS","STANDARD").anyMatch(s->ctx.getAttributes().get("deliveryType").equals(s));

    }
}
