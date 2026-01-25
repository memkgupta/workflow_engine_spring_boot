package com.mk.workflow_engine.implementation.nodes.shipping.express;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        name = "END_EXPRESS_SHIPPING",
        isRoot = false,
        type = "end",
        workflowId = "shipping:EXPRESS_SHIPPING:1"
)
public class ExpressDeliveryEnd extends WorkflowNode {

}
