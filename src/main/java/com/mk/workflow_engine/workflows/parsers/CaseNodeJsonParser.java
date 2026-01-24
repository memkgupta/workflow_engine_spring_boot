package com.mk.workflow_engine.workflows.parsers;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.WorkflowBuildContext;
import com.mk.workflow_engine.workflows.WorkflowDefinition;
import com.mk.workflow_engine.workflows.definitions.CaseNodeDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import com.mk.workflow_engine.workflows.strategies.NodeParser;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

@Component
public class CaseNodeJsonParser extends NodeParser<JsonNode> {



    @Override
    public NodeDefinition parse(JsonNode input, WorkflowDefinition workflowDefinition) throws WorkflowParseException {

        String name = input.path("name").asText(null);
        JsonNode cases = input.path("cases");

        if (name == null)
            throw new WorkflowParseException("Case node missing name");

        if (!cases.isObject())
            throw new WorkflowParseException("Case node '" + name + "' missing cases object");

        Map<String,String> caseMap = new HashMap<>();
        cases.forEachEntry((key, value) ->
                caseMap.put(key,this.getLinkingId(workflowDefinition,value.asString()))
        );
        CaseNodeDefinition def = new CaseNodeDefinition();
        def.setName(name);
        def.setType(NodeTypeEnum.CASE);
        def.setCases(caseMap);
        def.setWorkflowId(workflowDefinition.getId());
        return def;
    }

    @Override
    public boolean supports(NodeTypeEnum type) {
        return type == NodeTypeEnum.CASE;
    }
}
