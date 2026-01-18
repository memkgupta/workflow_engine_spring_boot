package com.mk.workflow_engine.exceptions;

public class WorkflowParseException extends RuntimeException {
    public WorkflowParseException(String message) {
        super(message);
    }
    public WorkflowParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
