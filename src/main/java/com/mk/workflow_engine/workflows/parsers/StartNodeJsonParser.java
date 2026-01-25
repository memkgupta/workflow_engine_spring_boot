package com.mk.workflow_engine.workflows.parsers;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.WorkflowBuildContext;
import com.mk.workflow_engine.workflows.WorkflowDefinition;
import com.mk.workflow_engine.workflows.definitions.StartNodeDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import com.mk.workflow_engine.workflows.strategies.NodeParser;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class StartNodeJsonParser extends NodeParser<JsonNode> {


    @Override
    public NodeDefinition parse(JsonNode input , WorkflowDefinition workflowDefinition) throws WorkflowParseException {
        String nodeId = input.get("name").asString();
        StartNodeDefinition definition = new StartNodeDefinition();
        definition.setName(nodeId);
        definition.setType(NodeTypeEnum.START);
        definition.setWorkflowId(workflowDefinition.getId());
        definition.setNextId(this.getLinkingId(
                workflowDefinition,input.get("next").asString()
        ));

        return definition;
    }

    @Override
    public boolean supports(NodeTypeEnum type) {
        return NodeTypeEnum.START.equals(type);
    }
}
