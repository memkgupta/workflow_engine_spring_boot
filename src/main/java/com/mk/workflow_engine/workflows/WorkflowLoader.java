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
    private final Map<String,NodeDefinition> nodeDefinitions = new HashMap<>();
// loads workflows from the storage and maintains the mapping of workflow names and the respective serialized representation
    public WorkflowLoader(WorkflowLoadingStrategy strategy) {
        workflowDefinitions.addAll(strategy.loadWorkflows());
        nodeDefinitions.putAll(strategy.getNodes());
    }
    public Set<String> getWorkflowNames() {
        return workflowDefinitions.stream().map(WorkflowDefinition::getName).collect(Collectors.toSet());
    }
    public NodeDefinition getNodeDefinition(String nodeId) {
        return nodeDefinitions.get(nodeId);
    }
}
