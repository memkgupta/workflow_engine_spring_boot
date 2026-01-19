package com.mk.workflow_engine.workflows.strategies;

public interface WorkflowDeserializer<I,O> {
    public O deserialize(I input);
}
