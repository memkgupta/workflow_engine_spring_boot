package com.mk.workflow_engine;

import java.util.UUID;

public interface StatePersistenceService {
     void persist(WorkflowContext ctx);
     WorkflowSession findById(UUID workflowId);
     void cancel(WorkflowContext ctx);
     void complete(WorkflowContext ctx);
     void spawnChild(WorkflowContext ctx);
}
