package com.mk.workflow_engine.workflows.definitions;

import com.mk.workflow_engine.workflows.NodeDefinition;

public class TaskNodeDefinition extends NodeDefinition {
    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getInvalidCause() {
        return "";
    }
}
