package com.mk.workflow_engine;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;

public abstract class CaseNode<T> extends WorkflowNode {
    @Getter
    private final Map<T,WorkflowNode> cases;
    private final Function<WorkflowContext,T> caseChecker;
    public CaseNode(Map<T,WorkflowNode> cases, Function<WorkflowContext, T> caseChecker)
    {
        this.cases = cases;
        this.caseChecker = caseChecker;
    }

    @Override
    protected WorkflowNode getNext(WorkflowContext ctx) {
        return cases.get(caseChecker.apply(ctx));
    }
}
