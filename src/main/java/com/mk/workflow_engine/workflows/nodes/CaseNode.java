package com.mk.workflow_engine.workflows.nodes;

import com.mk.workflow_engine.WorkflowContext;
import com.mk.workflow_engine.WorkflowNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class CaseNode<T> extends WorkflowNode {
    @Getter
    private final Map<T,WorkflowNode> cases;
    private final Function<WorkflowContext,T> caseChecker;
    public CaseNode( Function<WorkflowContext, T> caseChecker)
    {
        this.cases = new HashMap<>();
        this.caseChecker = caseChecker;
    }

    @Override
    protected WorkflowNode getNext(WorkflowContext ctx) {
        return cases.get(caseChecker.apply(ctx));
    }
}
