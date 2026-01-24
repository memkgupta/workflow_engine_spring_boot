package com.mk.workflow_engine.workflows.definitions;

import com.mk.workflow_engine.workflows.NodeDefinition;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class CaseNodeDefinition extends NodeDefinition {
    @Getter
    @Setter
    private Map<String,String> cases;

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getInvalidCause() {
        return "";
    }
}
