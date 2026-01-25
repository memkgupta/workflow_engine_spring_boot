package com.mk.workflow_engine.implementation.nodes.shipping.standard;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        name = "ASSIGN_NORMAL_PACKER_TASK",
        isRoot = false,
        type = "task",
        workflowId = "shipping:STANDARD_SHIPPING:1"
)
public class AssignNormalPacker extends WorkflowNode {
    public AssignNormalPacker() {
        super();
        super.task = (ctx)->{
            System.out.println("AssignNormalPacker");
        };
    }
}
