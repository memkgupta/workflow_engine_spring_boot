package com.mk.workflow_engine.implementation.nodes.shipping.express;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        name = "START_EXPRESS_SHIPPING",
        isRoot = true,
        type = "start",
        workflowId = "shipping:EXPRESS_SHIPPING:1"
)
public class StartExpressDeliveryFlow extends WorkflowNode {
    public StartExpressDeliveryFlow() {
        super();
        super.isValidState = (ctx)->{
            return
                    ctx.getAttributes().get("orderId")!=null
                    &&
                            ctx.getAttributes().get("paymentId")!=null;
        };


    }
}
