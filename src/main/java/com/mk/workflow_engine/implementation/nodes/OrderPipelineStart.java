package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        type = "start",
        id = "ORDER_START",
        isRoot = true,
        workflowName = "ORDER_PIPELINE"
)
public class OrderPipelineStart extends WorkflowNode {
public OrderPipelineStart() {
    super();

}
}
