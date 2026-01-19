package com.mk.workflow_engine.implementation.nodes.shipping.standard;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;

@Node(
        id = "ASSIGN_NORMAL_PACKER",
        isRoot = false,
        type = "task",
        workflowName = "STANDARD_SHIPPING"
)
public class AssignNormalPacker extends WorkflowNode {
    public AssignNormalPacker() {
        super();
        super.task = (ctx)->{
            System.out.println("AssignNormalPacker");
        };
    }
}
