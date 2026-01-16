package com.mk.workflow_engine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Entity
@Getter
@Setter
public class WorkflowSession{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ElementCollection
    private HashMap<String,Object> attributes;
    private Object entityIdentifier;
    private String entityName;
    private String currentNode;
    private boolean isActive;
    private String workflowName;
    @OneToOne
    private WorkflowSession parent;
    private Timestamp triggeredAt;
    private Timestamp completedAt;
}
