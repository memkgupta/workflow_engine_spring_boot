package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.WorkflowNode;
import com.mk.workflow_engine.annotations.Node;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WorkflowNodeRegistry {
    // fetch all the beans of the WorkflowNode
    private final Map<String, WorkflowNode> nodes = new HashMap<>();

    public WorkflowNodeRegistry(ApplicationContext applicationContext) {


        Map<String,WorkflowNode> beans =  applicationContext.getBeansOfType(WorkflowNode.class);
        for(WorkflowNode node :beans.values())
        {
            Node annotation = node.getClass().getAnnotation(Node.class);
            if(annotation == null)
            {
                throw new IllegalStateException(
                        "WorkflowNode bean " + node.getClass().getName() + " is missing @Node annotation"
                );
            }

            String id = annotation.id();
            nodes.put(id,node);

        }
    }

    public WorkflowNode getWorkflowNode(String id) {
        return nodes.get(id);
    }


}
