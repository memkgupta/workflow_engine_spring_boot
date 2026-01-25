package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.ConditionNode;
import com.mk.workflow_engine.annotations.Node;
import com.mk.workflow_engine.implementation.Order;
import com.mk.workflow_engine.implementation.OrderService;

import java.util.UUID;

@Node(
        type = "condition",
        name = "CHECK_PAYMENT_SUCCESS",
        isRoot = false,
        workflowId = "order:ORDER_PIPELINE:1"
)
public class PaymentSuccessNode extends ConditionNode {
    public PaymentSuccessNode(OrderService orderService) {
        super();
        super.isValidState = (ctx)->{
            return ctx.getAttributes().containsKey("orderId") && ctx.getAttributes().containsKey("paymentId");
        };
        super.setCondition(
                (ctx)->{
                    UUID orderId = (UUID) ctx.getAttributes().get("orderId");
                Order order = orderService.get(orderId);
                if(order==null){
                    return false;
                }
                if(order.getPaymentSuccess())
                    return true;
                else
                    return false;
                }
        );
        super.task = (ctx)->{
            // fetch payment status or any other task;
        };
    }
}
