package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        type = "end",
        id = "ORDER_END",
        isRoot = true,
        workflowName = "ORDER_PIPELINE"
)
public class OrderPipelineEnd extends WorkflowNode {
}
