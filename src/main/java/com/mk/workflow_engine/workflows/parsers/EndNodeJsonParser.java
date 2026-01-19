package com.mk.workflow_engine.workflows.parsers;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import com.mk.workflow_engine.workflows.strategies.NodeParser;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class EndNodeJsonParser implements NodeParser<JsonNode> {

    @Override
    public NodeDefinition parse(JsonNode input) {

        String name = input.path("name").asText(null);
        if (name == null)
            throw new WorkflowParseException("End node missing name");

        EndNodeDefinition def = new EndNodeDefinition();
        def.setName(name);
        def.setType("end");

        return def;
    }

    @Override
    public boolean supports(NodeTypeEnum type) {
        return type == NodeTypeEnum.END;
    }
}
