package com.mk.workflow_engine.workflows.parsers;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import com.mk.workflow_engine.workflows.strategies.NodeParser;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class TaskNodeJsonParser implements NodeParser<JsonNode> {

    @Override
    public NodeDefinition parse(JsonNode input) {

        String name = input.path("name").asString(null);
        String next = input.path("next").asString(null);

        if (name == null)
            throw new WorkflowParseException("Task node missing name");

        if (next == null)
            throw new WorkflowParseException("Task node '" + name + "' missing next");

        TaskNodeDefinition def = new TaskNodeDefinition();
        def.setName(name);
        def.setType("task");
        def.setNext(next);

        return def;
    }

    @Override
    public boolean supports(NodeTypeEnum type) {
        return type == NodeTypeEnum.TASK;
    }
}
