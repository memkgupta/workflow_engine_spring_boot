package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract   class NodeDefinition {
    protected String name;
    protected NodeTypeEnum type;
    protected String workflowId;
    protected NodeDefinition next;
    protected String nextId;
    public String getId() {
        return workflowId+"#"+name;
    }
    public abstract boolean validate();
    public abstract String getInvalidCause();

}
