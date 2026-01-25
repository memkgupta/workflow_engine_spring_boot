package com.mk.workflow_engine.implementation.nodes.shipping.express;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        name = "ASSIGN_PRIORITY_PACKER_TASK",
        isRoot = false,
        type = "task",
        workflowId = "shipping:EXPRESS_SHIPPING:1"
)
public class AssignPriorityPacker extends WorkflowNode {
    public AssignPriorityPacker() {
        super();
        super.task = (ctx)->{
            System.out.println("AssignPriorityPacker");
        };
    }
}
