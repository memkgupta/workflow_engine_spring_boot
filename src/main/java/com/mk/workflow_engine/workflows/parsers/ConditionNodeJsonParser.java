package com.mk.workflow_engine.workflows.parsers;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import com.mk.workflow_engine.workflows.strategies.NodeParser;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class ConditionNodeJsonParser implements NodeParser<JsonNode> {

    @Override
    public NodeDefinition parse(JsonNode input) {

        String name = input.path("name").asText(null);
        String yes = input.path("YES").asText(null);
        String no = input.path("NO").asText(null);

        if (name == null)
            throw new WorkflowParseException("Condition node missing name");

        if (yes == null || no == null)
            throw new WorkflowParseException("Condition node '" + name + "' missing YES/NO");

        ConditionNodeDefinition def = new ConditionNodeDefinition();
        def.setName(name);
        def.setType("condition");
        def.setYes(yes);
        def.setNo(no);

        return def;
    }

    @Override
    public boolean supports(NodeTypeEnum type) {
        return type == NodeTypeEnum.CONDITION;
    }
}
