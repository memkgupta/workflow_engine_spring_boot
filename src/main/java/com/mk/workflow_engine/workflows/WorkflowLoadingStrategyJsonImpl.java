package com.mk.workflow_engine.workflows;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.strategies.WorkflowLoadingStrategy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WorkflowLoadingStrategyJsonImpl implements WorkflowLoadingStrategy {
    private final WorkflowParser<JsonNode,WorkflowDefinition> parser;
    private final ResourcePatternResolver resolver;
    private final ObjectMapper mapper;

    public WorkflowLoadingStrategyJsonImpl(WorkflowParser<JsonNode,WorkflowDefinition> parser, ResourcePatternResolver resolver, ObjectMapper mapper) {
        this.parser = parser;
        this.resolver = resolver;
        this.mapper = mapper;
    }
    @Override
    public List<WorkflowDefinition> loadWorkflows() {

        List<WorkflowDefinition> workflowDefinitions = new ArrayList<>();
        try{
            Resource[] resources =
                    resolver.getResources("classpath:workflows/*.json");
            for(Resource resource : resources){
                try(InputStream inputStream = resource.getInputStream()){
                    JsonNode tree = mapper.readTree(inputStream);
                    workflowDefinitions.add(parser.parseWorkflow(tree));
                }
            }
        }
        catch (IOException ex){
            throw new WorkflowParseException("Could not load workflows", ex);
        }
        return workflowDefinitions;
    }




}
