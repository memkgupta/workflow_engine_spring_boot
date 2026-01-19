package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.strategies.WorkflowDeserializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class WorkflowParser<T,O> {
    ///  will be used by the loading strategy to parse the workflow files or anything
    private final WorkflowDeserializer<T,O> deserializer;

    public WorkflowParser(WorkflowDeserializer<T, O> deserializer) {

        this.deserializer = deserializer;
    }
    public abstract O parseWorkflow(T workflow) throws WorkflowParseException;


}
