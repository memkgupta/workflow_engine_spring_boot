package com.mk.workflow_engine.implementation.nodes.shipping.express;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        id = "ASSIGN_PRIORITY_PACKER",
        isRoot = false,
        type = "task",
        workflowName = "EXPRESS_SHIPPING"
)
public class AssignPriorityPacker extends WorkflowNode {
    public AssignPriorityPacker() {
        super();
        super.task = (ctx)->{
            System.out.println("AssignPriorityPacker");
        };
    }
}
