package com.mk.workflow_engine.workflows.definitions;

import com.mk.workflow_engine.workflows.NodeDefinition;
import lombok.Getter;
import lombok.Setter;

public class ConditionNodeDefinition extends NodeDefinition {
    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getInvalidCause() {
        return "";
    }
    @Getter
    @Setter
    private String positiveId;
    @Getter
    @Setter
    private String negativeId;
}
