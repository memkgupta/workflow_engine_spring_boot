package com.mk.workflow_engine;

import lombok.Builder;
import lombok.Setter;

import java.util.function.Function;


public abstract class ConditionNode extends WorkflowNode{
    @Setter
    private WorkflowNode positive;
    @Setter
    private WorkflowNode negative;
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
