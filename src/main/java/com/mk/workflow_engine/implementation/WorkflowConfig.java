package com.mk.workflow_engine.implementation;

import com.mk.workflow_engine.WorkflowFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Configuration
public class WorkflowConfig {
    private final ResourcePatternResolver resolver;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;
    public WorkflowConfig(ResourcePatternResolver resolver, ObjectMapper objectMapper, ApplicationContext applicationContext) {
        this.resolver = resolver;
        this.objectMapper = objectMapper;
        this.applicationContext = applicationContext;
    }
//    @Bean
//    public WorkflowFactory workflowFactory() {
//        WorkflowFactory factory = new WorkflowFactory(resolver, objectMapper, applicationContext);
//        try {
//            factory.init();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return factory;
//    }
}
