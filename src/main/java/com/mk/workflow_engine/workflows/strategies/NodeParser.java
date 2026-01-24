package com.mk.workflow_engine.workflows.strategies;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.WorkflowBuildContext;
import com.mk.workflow_engine.workflows.WorkflowDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public abstract class NodeParser<I> {
    public abstract NodeDefinition parse(I input, WorkflowDefinition workflowDefinition) throws WorkflowParseException;
   public abstract boolean supports(NodeTypeEnum type);
   public String getLinkingId(WorkflowDefinition workflowDefinition , String id) {

       if(workflowDefinition.getDependsOn().contains(id)){
           // another workflow
           return id;
       }
       else {
           return workflowDefinition.getId()+"#"+id;
       }
   }
}
