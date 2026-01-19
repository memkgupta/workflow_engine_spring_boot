package com.mk.workflow_engine.workflows.strategies;


import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.WorkflowDefinition;

import java.util.List;
import java.util.Map;

public interface WorkflowLoadingStrategy {
    List<WorkflowDefinition> loadWorkflows();


}
