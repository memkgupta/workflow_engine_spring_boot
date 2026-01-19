package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.exceptions.WorkflowBuildException;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowBuilder {


    private final WorkflowBuildContext ctx;
    private final NodeHandlerFactory  nodeHandlerFactory;
    public WorkflowBuilder( WorkflowBuildContext ctx, NodeHandlerFactory nodeHandlerFactory) {



        this.ctx = ctx;

        this.nodeHandlerFactory = nodeHandlerFactory;
    }
    public WorkflowNode build(WorkflowDefinition workflowDefinition)
    {
    //todo complete
        List<NodeDefinition> nodes =  workflowDefinition.getNodes();
        WorkflowNode root = new WorkflowNode() {
        };
        NodeDefinition startNode = nodes.stream().filter(n->n.getType().equals(NodeTypeEnum.START)).findFirst().orElse(null);
        if(startNode == null)
        {
            throw new WorkflowBuildException("Workflow start node missing next of the workflow "+workflowDefinition.getName());
        }
        root.setNext(rec(startNode.getNext().getName(),nodes));
        return root;
    }
    private WorkflowNode rec(String nodeName, List<NodeDefinition> nodes)
    {
        if(nodeName.contains("#"))
        {
            // node
            NodeDefinition nodeDefinition = nodes.stream().filter(
                    node->node.getId().equals(nodeName)
            ).findFirst().orElseThrow(
                    () -> new WorkflowBuildException("Node "+nodeName+" not found in workflow definition")
            );
            NodeHandler handler = nodeHandlerFactory.getHandler(nodeDefinition.getType());
            return handler.build(nodeDefinition,ctx);
        }
        else {
            // another_workflow
            if(!ctx.getDefinitions().containsKey(nodeName)) throw new WorkflowBuildException("Workflow "+nodeName+" not found");
            return build(ctx.getDefinitions().get(nodeName));
        }

    }
}
