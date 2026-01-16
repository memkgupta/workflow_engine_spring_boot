package com.mk.workflow_engine;

import com.mk.workflow_engine.annotations.Node;
import jakarta.transaction.Transactional;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;


public class WorkflowContext{
    private final StatePersistenceService persistenceService;
    @Getter
    protected HashMap<String,Object> attributes;
    private String workflowName;
    public WorkflowContext(StatePersistenceService persistenceService, String entityName, Object entityIdentifier) {
        this.persistenceService = persistenceService;
        this.entityName = entityName;
        this.entityIdentifier = entityIdentifier;

    }
    protected final String entityName;
    protected final Object entityIdentifier;
    protected UUID sessionId;
    @Getter
    protected boolean isCancelled;
    protected WorkflowNode current;
    @Transactional
    protected void execute(WorkflowContext ctx , Consumer<WorkflowContext> consumer)
    {
        // this will be transactional
        consumer.accept(ctx);
        persistenceService.persist(ctx);
    }

    public void moveToNext()
    {
        this.current = current.getNext(this);

    }


}
