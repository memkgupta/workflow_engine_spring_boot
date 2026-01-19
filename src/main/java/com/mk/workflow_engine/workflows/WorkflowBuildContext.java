package com.mk.workflow_engine.workflows;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WorkflowBuildContext {
    @Getter
    private final Map<String,WorkflowDefinition> definitions = new HashMap<>();
    public WorkflowBuildContext(WorkflowNodeDependencyResolver dependencyResolver, WorkflowNodeRegistry workflowNodeRegistry, WorkflowRegistry workflowRegistry) {
        for(WorkflowDefinition d : dependencyResolver.getSortedWorkflows())
        {
            definitions.put(d.getId(), d);
        }
    }
}
