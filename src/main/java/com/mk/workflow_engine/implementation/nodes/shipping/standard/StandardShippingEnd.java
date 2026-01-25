package com.mk.workflow_engine.implementation.nodes.shipping.standard;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        name = "STANDARD_SHIPPING_END",
        isRoot = false,
        type = "end",
        workflowId = "shipping:STANDARD_SHIPPING:1"
)
public class StandardShippingEnd extends WorkflowNode {
}
