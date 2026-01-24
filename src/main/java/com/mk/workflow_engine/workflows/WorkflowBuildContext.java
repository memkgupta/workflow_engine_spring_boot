package com.mk.workflow_engine.workflows;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


public class WorkflowBuildContext {
    @Getter
    private final Map<String,WorkflowDefinition> definitions = new HashMap<>();
    @Getter
    private final WorkflowDefinition workflowDefinition;
    @Getter
    private final Map<String,NodeDefinition> nodes = new HashMap<>();
    public WorkflowBuildContext(WorkflowDefinition workflowDefinition,WorkflowNodeDependencyResolver nodeResolver) {

        this.workflowDefinition = workflowDefinition;
        for(NodeDefinition nodeDefinition : workflowDefinition.getNodes()) {
            nodes.put(nodeDefinition.getName(), nodeDefinition);
        }
        for(WorkflowDefinition def : nodeResolver.getSortedWorkflows())
        {
            definitions.put(def.getName(), def);
        }


    }
}
