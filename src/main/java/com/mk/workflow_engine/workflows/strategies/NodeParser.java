package com.mk.workflow_engine.workflows.strategies;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;

public interface NodeParser<I> {
    NodeDefinition parse(I input) throws WorkflowParseException;
    boolean supports(NodeTypeEnum type);
}
