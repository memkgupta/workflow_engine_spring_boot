package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import com.mk.workflow_engine.workflows.strategies.NodeParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public  class NodeParserFactory<I> {

    private final List<NodeParser<I>> parsers;

    public NodeParserFactory(List<NodeParser<I>> parsers) {
        this.parsers = parsers;
    }

    public  NodeParser<I> getParser(NodeTypeEnum type) {
        return parsers.stream()
                .filter(p -> p.supports(type))
                .findFirst()
                .orElseThrow(() ->
                        new WorkflowParseException("No NodeParser found for type: " + type));
    }
}
