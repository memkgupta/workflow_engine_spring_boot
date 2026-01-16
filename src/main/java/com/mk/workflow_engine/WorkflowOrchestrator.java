package com.mk.workflow_engine;

import com.mk.workflow_engine.exceptions.InvalidWorkflowStateException;
import com.mk.workflow_engine.exceptions.WorkflowTransitionException;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.UUID;


public class WorkflowOrchestrator {
    private final WorkflowFactory workflowFactory;
    private final StatePersistenceService persistenceService;
    public WorkflowOrchestrator(WorkflowFactory workflowFactory, StatePersistenceService persistenceService) {
        this.workflowFactory = workflowFactory;
        this.persistenceService = persistenceService;
    }
    public WorkflowContext initiate(String workflow_name , HashMap<String,Object> initState , String entityName , Object entityIdentifier)
    {
    // start the workflow and persist
        //1. Get the root from the Workflow factory
        //2. Create the context with initial state
        //3. Create the session
    WorkflowNode root = workflowFactory.getWorkFlow(workflow_name);
    WorkflowContext ctx = new WorkflowContext(persistenceService,entityName,entityIdentifier);
    ctx.current = root;
    ctx.attributes = new HashMap<>(initState);
    persistenceService.persist(ctx);
    return ctx;
    }
    @Transactional
    public void transition(UUID workflowId) {
        step(fetchContext(workflowId));
    }

    @Transactional
    public void resume(UUID workflowId) {
        WorkflowContext ctx = fetchContext(workflowId);
        while (ctx.current != null) {
         step(ctx);
        }
    }

    private void step(WorkflowContext ctx) {
        if (!ctx.current.isValidState.apply(ctx)) {
            throw new InvalidWorkflowStateException("ctx");
        }

        ctx.execute(ctx, ctx.current.task);

        if (!ctx.current.conditionalMove.apply(ctx)) {
            throw new WorkflowTransitionException("ctx");
        }

        ctx.moveToNext();
        persistenceService.persist(ctx);


    }

    public void cancel(UUID workflowId)
    {
        WorkflowContext ctx = fetchContext(workflowId);
        ctx.isCancelled = true;
        persistenceService.cancel(ctx);
        //todo implement cancel
    }
    private WorkflowContext fetchContext(UUID workflowId)
    {
        WorkflowSession session = persistenceService.findById(workflowId);
        WorkflowContext ctx = new WorkflowContext(persistenceService,
                session.getEntityName(),session.getEntityIdentifier()
        );
        ctx.attributes =  session.getAttributes();
        ctx.current = null; //todo replace this null with proper workflow tree traversal to the node with that id;
        return ctx;
    }
}
