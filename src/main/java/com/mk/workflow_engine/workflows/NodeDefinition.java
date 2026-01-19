package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract   class NodeDefinition {
    private String name;
    private NodeTypeEnum type;
    private String workflowId;
    private NodeDefinition next;
    public String getId() {
        return workflowId+"#"+name;
    }
    public abstract boolean validate();
    public abstract String getInvalidCause();
}
