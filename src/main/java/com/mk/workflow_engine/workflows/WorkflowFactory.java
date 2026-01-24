package com.mk.workflow_engine.workflows;


import com.mk.workflow_engine.WorkflowNode;
import org.springframework.stereotype.Component;

@Component
public class WorkflowFactory {
    private final WorkflowRegistry workflowRegistry;

    public WorkflowFactory(WorkflowRegistry workflowRegistry) {
        this.workflowRegistry = workflowRegistry;
    }

    public WorkflowNode getWorkFlow(String id)
    {
        return workflowRegistry.getWorkflow(id);
    }

}
