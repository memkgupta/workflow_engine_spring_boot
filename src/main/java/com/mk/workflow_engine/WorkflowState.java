package com.mk.workflow_engine;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class WorkflowState<T> {
    private T data;
    private int current;
    private Instant updatedAt;
    private Instant createdAt;

}

