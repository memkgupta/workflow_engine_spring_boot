package com.mk.workflow_engine.implementation.nodes.shipping.express;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpressShippingFlow {
    @Bean
    public AssignPriorityPacker assignPriorityPacker() {
        return new AssignPriorityPacker();
    }
    @Bean
    public StartExpressDeliveryFlow startExpressDeliveryFlow() {
        return new StartExpressDeliveryFlow();
    }
    @Bean
    public ExpressDeliveryEnd expressDeliveryEnd() {
        return new ExpressDeliveryEnd();
    }
}
