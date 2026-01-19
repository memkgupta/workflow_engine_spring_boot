package com.mk.workflow_engine.workflows;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WorkflowDefinition {
    String namespace;
    String name;
    int version;
    List<NodeDefinition> nodes;
    List<String> dependsOn;
    public String getId()
    {
        return namespace + ":" + name+":"+version;
    }
}