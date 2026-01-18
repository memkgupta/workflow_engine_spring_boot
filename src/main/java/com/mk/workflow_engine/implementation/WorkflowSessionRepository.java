package com.mk.workflow_engine.implementation;

import com.mk.workflow_engine.WorkflowSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkflowSessionRepository extends JpaRepository<WorkflowSession, UUID> {
}
