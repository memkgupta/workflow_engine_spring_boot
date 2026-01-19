package com.mk.workflow_engine.implementation.nodes.shipping.express;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        id = "EXPRESS_SHIPPING_START",
        isRoot = true,
        type = "start",
        workflowName = "EXPRESS_SHIPPING"
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
