package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        type = "end",
        name = "ORDER_END",
        isRoot = true,
        workflowId = "order:ORDER_PIPELINE:1"
)
public class OrderPipelineEnd extends WorkflowNode {
}
