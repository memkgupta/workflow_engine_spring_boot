package com.mk.workflow_engine;

import lombok.Builder;
import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Function;


public abstract class WorkflowNode {
    @Setter
    private WorkflowNode next;

    protected Function<WorkflowContext,Boolean> conditionalMove;
    protected Consumer<WorkflowContext> task;
    protected Function<WorkflowContext,Boolean> isValidState;
    public WorkflowNode()
    {

        this.conditionalMove = (ctx)->true;
        this.task = (ctx)->{};
        this.isValidState = (ctx)->true;
    }

    protected WorkflowNode getNext(WorkflowContext ctx){
        return this.next;
    }

}
