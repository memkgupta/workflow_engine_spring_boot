package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.exceptions.WorkflowBuildException;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NodeHandlerFactory {

    private final List<NodeHandler> handlers;

    public NodeHandlerFactory(List<NodeHandler> handlers) {
        this.handlers = handlers;
    }

    public NodeHandler getHandler(NodeTypeEnum type) {
        return handlers.stream()
                .filter(h -> h.supports(type))
                .findFirst()
                .orElseThrow(() ->
                        new WorkflowBuildException("No handler found for node type: " + type));
    }
}
