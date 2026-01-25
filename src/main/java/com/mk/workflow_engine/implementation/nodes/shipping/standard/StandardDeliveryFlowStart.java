package com.mk.workflow_engine.implementation.nodes.shipping.standard;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        name = "START_STANDARD_SHIPPING",
        isRoot = true,
        type = "start",
        workflowId = "shipping:STANDARD_SHIPPING:1"
)
public class StandardDeliveryFlowStart extends WorkflowNode {
    public StandardDeliveryFlowStart() {
        super();
        super.isValidState = (ctx)->{
            return
                    ctx.getAttributes().get("orderId")!=null
                            &&
                            ctx.getAttributes().get("paymentId")!=null;
        };
    }
}
