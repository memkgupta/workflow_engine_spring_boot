package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.workflows.strategies.WorkflowLoadingStrategy;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Component
public class WorkflowLoader {
    private final List<WorkflowDefinition> workflowDefinitions = new ArrayList<>();

// loads workflows from the storage and maintains the mapping of workflow names and the respective serialized representation
    public WorkflowLoader(WorkflowLoadingStrategy strategy) {
        workflowDefinitions.addAll(strategy.loadWorkflows());
//        nodeDefinitions.putAll(strategy.getNodes());
    }
    public Set<String> getWorkflowNames() {
        return workflowDefinitions.stream().map(WorkflowDefinition::getId).collect(Collectors.toSet());
    }

}
