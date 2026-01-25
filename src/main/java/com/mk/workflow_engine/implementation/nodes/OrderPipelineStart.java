package com.mk.workflow_engine.implementation.nodes;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        type = "start",
        name = "START_ORDER_WORKFLOW",
        isRoot = true,
        workflowId = "order:ORDER_PIPELINE:1"
)
public class OrderPipelineStart extends WorkflowNode {
public OrderPipelineStart() {
    super();

}
}
