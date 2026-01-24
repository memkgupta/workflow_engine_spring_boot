package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.CaseNode;
import com.mk.workflow_engine.ConditionNode;
import com.mk.workflow_engine.WorkflowContext;
import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.exceptions.WorkflowBuildException;
import com.mk.workflow_engine.exceptions.WorkflowConfigurationException;
import com.mk.workflow_engine.exceptions.WorkflowDependencyException;
import com.mk.workflow_engine.workflows.definitions.CaseNodeDefinition;
import com.mk.workflow_engine.workflows.definitions.ConditionNodeDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WorkflowBuilder {



    private final WorkflowNodeDependencyResolver resolver;
    private final WorkflowNodeRegistry  workflowNodeRegistry;
    public WorkflowBuilder( WorkflowNodeDependencyResolver resolver, WorkflowNodeRegistry workflowNodeRegistry) {

        this.resolver = resolver;
        this.workflowNodeRegistry = workflowNodeRegistry;
    }
    public WorkflowNode build(WorkflowDefinition workflowDefinition)
    {
        List<NodeDefinition> nodes =  workflowDefinition.getNodes();
        WorkflowBuildContext context = new WorkflowBuildContext(
                workflowDefinition,resolver
        );
        WorkflowNode root = new WorkflowNode() {
        };
        NodeDefinition startNode = nodes.stream().filter(n->n.getType().equals(NodeTypeEnum.START)).findFirst().orElse(null);
        if(startNode == null)
        {
            throw new WorkflowBuildException("Workflow start node missing next of the workflow "+workflowDefinition.getName());
        }
        root.setNext(rec(startNode.getNextId(),nodes,context));
        return root;
    }

    private WorkflowNode rec(String nodeName, List<NodeDefinition> nodes,WorkflowBuildContext context)
    {

        if(nodeName.contains("#"))
        {
            // node
            NodeDefinition nodeDefinition = nodes.stream().filter(
                    node->node.getId().equals(nodeName)
            ).findFirst().orElseThrow(
                    () -> new WorkflowBuildException("Node "+nodeName+" not found in workflow definition")
            );
            NodeTypeEnum nodeType = nodeDefinition.getType();
            if(nodeType.equals(NodeTypeEnum.END)) return new WorkflowNode() {
                @Override
                public void setNext(WorkflowNode next) {

                }
                @Override
                protected WorkflowNode getNext(WorkflowContext ctx) {
                    return null;
                }
            };
            WorkflowNode workflowNode = workflowNodeRegistry.getWorkflowNode(nodeName);
            if (workflowNode == null ) {
                throw new WorkflowConfigurationException("No WorkflowNode bean mapped for node: " + nodeName);
            }
            if(nodeType.equals(NodeTypeEnum.CONDITION))
            {
                ConditionNodeDefinition cndDefinition = (ConditionNodeDefinition) nodeDefinition;
                ConditionNode cndNode = (ConditionNode) workflowNode;
                cndNode.setPositive(rec(cndDefinition.getPositiveId(),nodes,context));
                cndNode.setNegative(rec(cndDefinition.getNegativeId(),nodes,context));
            }
            else if(nodeType.equals(NodeTypeEnum.CASE))
            {
                CaseNodeDefinition caseNodeDefinition = (CaseNodeDefinition) nodeDefinition;
                CaseNode<Object> caseNode = (CaseNode<Object>) workflowNode;

                Map<String, String> casesMap = caseNodeDefinition.getCases();
                Map<Object, WorkflowNode> updated = new HashMap<>();

                for (Map.Entry<String, String> entry : casesMap.entrySet()) {
                    String key = entry.getKey();
                    WorkflowNode nxt;

                    String target = casesMap.get(key);
                    NodeDefinition targetNode = workflowNodeRegistry.getNodeDefinition(caseNodeDefinition.workflowId+"#"+target);
                    if(targetNode == null)
                    {
                        // another workflow
                        WorkflowDefinition workflowDefinition =
                                resolver.getSortedWorkflows().stream().filter(
                                        wd->wd.getId().equals(target)
                                ).findFirst().orElseThrow(
                                        ()-> new WorkflowConfigurationException("Workflow node "+target+" not found in workflow definition")
                                );
                        NodeDefinition start = workflowDefinition.getNodes()
                                .stream().filter(n->n.getType().equals(NodeTypeEnum.START)).findFirst().orElseThrow();

                        nxt = rec(
                        start.getId(),workflowDefinition.getNodes(),context
                        );
                    }
                    else {
                        nxt = rec(targetNode.getId(),nodes,context);
                    }
                    updated.put(key, nxt);
                }

                caseNode.getCases().clear();
                caseNode.getCases().putAll(updated);

            }  else {

                workflowNode.setNext(rec(nodeDefinition.getNextId(),nodes,context));
            }
            return workflowNode;

        }
        else {
            // another_workflow
            if(!context.getDefinitions().containsKey(nodeName)) throw new WorkflowBuildException("Workflow "+nodeName+" not found");
            return build(context.getDefinitions().get(nodeName));
        }






    }
}
