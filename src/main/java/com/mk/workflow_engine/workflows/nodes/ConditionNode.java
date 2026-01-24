package com.mk.workflow_engine.workflows.nodes;

import com.mk.workflow_engine.WorkflowContext;
import com.mk.workflow_engine.WorkflowNode;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

public abstract class ConditionNode extends WorkflowNode {
    @Setter
    private WorkflowNode positive;
    @Setter
    private WorkflowNode negative;
    @Setter
    @Getter
    private Function<WorkflowContext, Boolean> condition;

    @Override
    protected WorkflowNode getNext(WorkflowContext context) {
        if(this.condition.apply(context))
        {
            return positive;
        }
        else {
            return negative;
        }
    }
}