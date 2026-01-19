package com.mk.workflow_engine.implementation.nodes.shipping.standard;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        id = "STANDARD_SHIPPING_START",
        isRoot = true,
        type = "start",
        workflowName = "STANDARD_SHIPPING"
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
