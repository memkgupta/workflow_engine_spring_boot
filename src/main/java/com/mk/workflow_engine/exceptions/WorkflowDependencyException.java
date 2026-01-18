package com.mk.workflow_engine.exceptions;


public class WorkflowDependencyException extends RuntimeException {
    public WorkflowDependencyException(String message) {
        super(message);
    }
}
