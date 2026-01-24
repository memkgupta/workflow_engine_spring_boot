package com.mk.workflow_engine.workflows.strategies;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.WorkflowBuildContext;

public interface WorkflowDeserializer<I,O> {
    public O deserialize(I input) throws WorkflowParseException;
}
