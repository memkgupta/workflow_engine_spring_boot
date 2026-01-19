package com.mk.workflow_engine.implementation.nodes.shipping.standard;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        id = "STANDARD_SHIPPING_END",
        isRoot = false,
        type = "end",
        workflowName = "STANDARD_SHIPPING"
)
public class StandardShippingEnd extends WorkflowNode {
}
