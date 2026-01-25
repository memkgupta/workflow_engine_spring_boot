package com.mk.workflow_engine.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Node {
    String workflowId();
    String name();
    boolean isRoot();
    String type();
}
