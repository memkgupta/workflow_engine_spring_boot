package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import tools.jackson.databind.JsonNode;

public interface NodeHandler<T extends NodeDefinition> {
    boolean supports(NodeTypeEnum type);
    WorkflowNode build(T definition, WorkflowBuildContext ctx);
}
