package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        type = "task",
        id = "PAYMENT_FAILED",
        isRoot = false,
        workflowName = "ORDER_PIPELINE"
)
public class PaymentFailedNode extends WorkflowNode {
    public PaymentFailedNode() {
        super();
        super.task = (ctx)->{
            // send notification to user ;
            System.out.println(
                    ctx.getAttributes().get("orderId") +" Payment failed"
            );
        };

    }
}
