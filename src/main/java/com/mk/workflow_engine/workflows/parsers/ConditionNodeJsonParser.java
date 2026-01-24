package com.mk.workflow_engine.workflows.parsers;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.WorkflowBuildContext;
import com.mk.workflow_engine.workflows.WorkflowDefinition;
import com.mk.workflow_engine.workflows.definitions.ConditionNodeDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import com.mk.workflow_engine.workflows.strategies.NodeParser;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class ConditionNodeJsonParser extends NodeParser<JsonNode> {



    @Override
    public NodeDefinition parse(JsonNode input, WorkflowDefinition workflowDefinition) throws WorkflowParseException {

        String name = input.path("name").asString(null);
        String yes = input.path("YES").asString(null);
        String no = input.path("NO").asString(null);

        if (name == null)
            throw new WorkflowParseException("Condition node missing name");

        if (yes == null || no == null)
            throw new WorkflowParseException("Condition node '" + name + "' missing YES/NO");

        ConditionNodeDefinition def = new ConditionNodeDefinition();
        def.setName(name);
        def.setType(NodeTypeEnum.CONDITION);
        def.setWorkflowId(workflowDefinition.getId());
        def.setPositiveId(
               this.getLinkingId(workflowDefinition,yes)
        );

        def.setNegativeId(
                this.getLinkingId(workflowDefinition,no)
        );
        return def;
    }

    @Override
    public boolean supports(NodeTypeEnum type) {
        return type == NodeTypeEnum.CONDITION;
    }
}
