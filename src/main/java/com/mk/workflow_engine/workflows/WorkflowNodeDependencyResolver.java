package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.exceptions.WorkflowDependencyException;
import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import lombok.Getter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkflowNodeDependencyResolver {
    private final Map<String, Integer> indegree = new HashMap<>();

     private final HashMap<String,List<String>> dependencyGraph = new HashMap<>();
    @Getter
    private final List<WorkflowDefinition> sortedWorkflows = new ArrayList<>();
    private final WorkflowNodeRegistry nodeRegistry;
    public WorkflowNodeDependencyResolver(WorkflowLoader workflowLoader, WorkflowNodeRegistry nodeRegistry) {
        this.nodeRegistry = nodeRegistry;

        Set<String> workflowNames = workflowLoader.getWorkflowNames();
        // todo link the node to their next here



        for (WorkflowDefinition workflow :workflowLoader.getWorkflowDefinitions()) {

            String name = workflow.getId();
            indegree.putIfAbsent(name, 0);

            List<String> dependencies = workflow.getDependsOn();

            if (dependencies != null ) {

                dependencies.forEach(dep -> {
                    // Validate dependency exists
                    if (!workflowNames.contains(dep)) {
                        throw new WorkflowDependencyException(
                                "Workflow '" + name + "' depends on missing workflow '" + dep + "'");
                    }
                    // Build reverse graph: dependency → dependent workflow
                    dependencyGraph.putIfAbsent(dep, new ArrayList<>());
                    dependencyGraph.get(dep).add(name);

                    // Increase indegree of current workflow
                    indegree.put(name, indegree.get(name) + 1);
                });
            }
        }
        Map<String,WorkflowDefinition> workflowMaps =
            workflowLoader.getWorkflowDefinitions().stream().collect(Collectors.toMap(WorkflowDefinition::getId, workflow -> workflow));
        sortedWorkflows.addAll(
                topologicalSort(
                      workflowMaps,
                      indegree,
                      dependencyGraph
                )
        );
        buildChain(
                workflowLoader.getWorkflowDefinitions()
        );
    for(WorkflowDefinition workflow : sortedWorkflows){
        for(NodeDefinition node : workflow.getNodes()){
            nodeRegistry.registerDefinition(
                    node.getId(),node
            );
        }
    }
    }
    private static List<WorkflowDefinition> topologicalSort(Map<String,WorkflowDefinition> workflowMap , Map<String, Integer> indegree , Map<String, List<String>> dependencyGraph)
    {

        Queue<String> q = new LinkedList<>();

        for(var e : indegree.entrySet())
        {
            if(e.getValue() == 0)
            {
                q.add(e.getKey());
            }
        }

        List<WorkflowDefinition> res = new ArrayList<>();
        while(!q.isEmpty())
        {
            String key = q.poll();
            List<String> deps = dependencyGraph.getOrDefault(key, Collections.emptyList());
            res.add(workflowMap.get(key));
            for(var e : deps)
            {
                indegree.put(e, indegree.get(e)-1);
                if(indegree.get(e) == 0)
                {
                    q.offer(e);
                }
            }
        }

        if (res.size() != workflowMap.size()) {
            throw new WorkflowDependencyException("Cycle detected in workflow dependency graph");

        }
        return res;
    }
    private void buildChain(List<WorkflowDefinition> workflows)
    {
        for(WorkflowDefinition workflow : workflows)
        {
            List<NodeDefinition> nodeDefinitions = workflow.getNodes();
            Map<String,NodeDefinition> workflowNodes = new HashMap<>();
            workflowNodes.putAll(
                    nodeDefinitions.stream().collect(Collectors.toMap(NodeDefinition::getId, nodeDefinition -> nodeDefinition))
            );
            for(NodeDefinition nodeDefinition : nodeDefinitions)
            {
                if(!nodeDefinition.type.equals(NodeTypeEnum.TASK) && !nodeDefinition.type.equals(NodeTypeEnum.START)) continue;
                String nextId = nodeDefinition.getNextId();
                if(workflowNodes.containsKey(nextId))
                {
                    // node in the workflow itself
                    nodeDefinition.setNext(workflowNodes.get(nextId));
                }
                else {
                    WorkflowDefinition nextWorkflow = workflows
                            .stream().filter(
                                    w->w.getId().equals(nextId)
                            ).findFirst().orElse(null);
                    if(nextWorkflow == null)
                    {
                        throw new WorkflowParseException("No next "+workflow.getId()+" in workflow node "+nodeDefinition.getId()+" for next "+ nextId);
                    }
                    nodeDefinition.setNext(
                            nextWorkflow.getNodes().stream().filter(
                                    n->n.getType().equals(NodeTypeEnum.START)
                            ).findFirst().orElseThrow(() -> new WorkflowParseException("No next"))
                    );
                }
            }
        }
    }
}
