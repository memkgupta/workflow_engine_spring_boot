package com.mk.workflow_engine.exceptions;

public class StateNotPersistedException extends RuntimeException {
    public StateNotPersistedException(String message) {
        super(message);
    }
}
