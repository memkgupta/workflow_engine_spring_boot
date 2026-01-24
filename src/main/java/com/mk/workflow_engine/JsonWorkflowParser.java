package com.mk.workflow_engine;

import com.mk.workflow_engine.exceptions.WorkflowDependencyException;
import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.WorkflowDefinition;
import com.mk.workflow_engine.workflows.WorkflowParser;
import com.mk.workflow_engine.workflows.WorkflowRegistry;
import com.mk.workflow_engine.workflows.strategies.WorkflowDeserializer;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class JsonWorkflowParser extends WorkflowParser<JsonNode, WorkflowDefinition> {
    private final WorkflowDeserializer<JsonNode, WorkflowDefinition> deserializer;

    public JsonWorkflowParser(WorkflowDeserializer<JsonNode, WorkflowDefinition> deserializer) {
        super(deserializer);
        this.deserializer = deserializer;

    }

    @Override
    public WorkflowDefinition parseWorkflow(JsonNode workflow) throws WorkflowParseException {
       WorkflowDefinition workflowDefinition = deserializer.deserialize(workflow);
        validate(workflowDefinition);
        return workflowDefinition;
    }
    private void validate(WorkflowDefinition workflowDefinition) throws WorkflowParseException {
        if(workflowDefinition.getName() == null || workflowDefinition.getName().isEmpty()){
            throw new WorkflowParseException("Workflow name not found");
        }

        for(String dep : workflowDefinition.getDependsOn())
        {
            if(!workflowDefinition.getDependsOn().contains(dep))
            {
                throw new WorkflowDependencyException(
                        "Workflow '" + workflowDefinition.getId() + "' depends on missing workflow '" + dep + "'");
            }
            for(NodeDefinition node : workflowDefinition.getNodes())
            {
                if(!node.validate())
                {
                    throw new WorkflowParseException("Workflow '" + workflowDefinition.getId() + "' have a invalid node '" + node.getName() + "'"+" "+node.getInvalidCause() );
                }
            }
        }
    }


}
