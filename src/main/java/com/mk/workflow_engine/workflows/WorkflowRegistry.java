package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.WorkflowNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class WorkflowRegistry {
    //responsible for containing entire workflow graphs
    // responsible for building the graphs
    // will be directly used by the WorkflowFactory

    private final HashMap<String, WorkflowNode> workflows = new HashMap<>();
    public WorkflowRegistry(WorkflowNodeDependencyResolver dependencyResolver, WorkflowLoader loader , WorkflowBuilder workflowBuilder) {
    List<WorkflowDefinition> definitions = dependencyResolver.getSortedWorkflows();
    for(WorkflowDefinition workflow : definitions) {
      WorkflowNode root =   workflowBuilder.build(workflow);
      workflows.put(workflow.getId(), root);
    }
    }
    public Set<String> getWorkflowNames() {
        return workflows.keySet();
    }
    public WorkflowNode getWorkflow(String id)
    {
        return workflows.get(id);
    }
}
