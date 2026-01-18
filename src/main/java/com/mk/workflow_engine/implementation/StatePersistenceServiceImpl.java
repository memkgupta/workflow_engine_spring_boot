package com.mk.workflow_engine.implementation;

import com.mk.workflow_engine.StatePersistenceService;
import com.mk.workflow_engine.WorkflowContext;
import com.mk.workflow_engine.WorkflowSession;
import com.mk.workflow_engine.exceptions.InvalidContextException;
import com.mk.workflow_engine.exceptions.StateNotPersistedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
public class StatePersistenceServiceImpl implements StatePersistenceService {
    private WorkflowSessionRepository repository;

    @Override
    public void persist(WorkflowContext ctx) {
        WorkflowSession session;
        if(ctx.getSessionId() == null){
        // create new session
            session = new WorkflowSession();
        session.setActive(true);
        session.setAttributes(ctx.getAttributes());
        session.setEntityName(ctx.getEntityName());
        session.setEntityIdentifier(ctx.getEntityIdentifier());
        session.setTriggeredAt(Timestamp.from(Instant.now()));
        session.setWorkflowName(ctx.getWorkflowName());
        }
    else {
        // update the session
            session = repository.findById(ctx.getSessionId()).orElse(null);
        if(session == null){
            throw new InvalidContextException("Invalid session id " + ctx.getSessionId());
        }
//        session.setActive(true);
        session.setAttributes(ctx.getAttributes());
        session.setEntityName(ctx.getEntityName());
        session.setCurrentNode(ctx.getWorkflowName());
        }
        repository.save(session);
    }

    @Override
    public WorkflowSession findById(UUID workflowId) {
        return repository.findById(workflowId).orElse(null);
    }

    @Override
    public void cancel(WorkflowContext ctx) {
       if(ctx.getSessionId() == null){
           throw new StateNotPersistedException("");
       }
       WorkflowSession session = repository.findById(ctx.getSessionId()).orElse(null);
       if(session == null){
           throw new InvalidContextException("Invalid session id " + ctx.getSessionId());
       }
       session.setActive(false);
       repository.save(session);
       // todo call rollback actions if specified
    }

    @Override
    public void complete(WorkflowContext ctx) {
        if(ctx.getSessionId() == null){
            throw new StateNotPersistedException("");
        }
        WorkflowSession session = findById(ctx.getSessionId());
        if(session == null){
            throw new InvalidContextException("Invalid session id " + ctx.getSessionId());
        }
        session.setCompletedAt(Timestamp.from(Instant.now()));
        session.setActive(false);
        repository.save(session);
    }

    @Override
    public void spawnChild(WorkflowContext ctx) {

    }
}
