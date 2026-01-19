package com.mk.workflow_engine.implementation.nodes.shipping.standard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StandardShippingFlow {
    @Bean
    public StandardDeliveryFlowStart standardDeliveryFlowStart() {
        return new StandardDeliveryFlowStart();
    }
    @Bean
    public AssignNormalPacker  assignNormalPacker() {
        return new AssignNormalPacker();
    }
    @Bean
    public StandardShippingEnd  standardShippingEnd() {
        return new StandardShippingEnd();
    }
}

