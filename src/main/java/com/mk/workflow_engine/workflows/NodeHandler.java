package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import tools.jackson.databind.JsonNode;

public interface NodeHandler {
    boolean supports(NodeTypeEnum type);
    WorkflowNode build(NodeDefinition definition, WorkflowBuildContext ctx);
}
