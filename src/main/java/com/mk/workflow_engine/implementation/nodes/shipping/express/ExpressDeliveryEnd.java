package com.mk.workflow_engine.implementation.nodes.shipping.express;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        id = "EXPRESS_SHIPPING_END",
        isRoot = false,
        type = "end",
        workflowName = "EXPRESS_SHIPPING"
)
public class ExpressDeliveryEnd extends WorkflowNode {

}
