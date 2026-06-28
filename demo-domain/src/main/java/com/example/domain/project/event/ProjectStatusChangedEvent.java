package com.example.domain.project.event;

import com.example.common.event.DomainEvent;
import com.example.domain.project.model.Project;

import java.time.Instant;

public record ProjectStatusChangedEvent(
        Long projectId,
        String projectName,
        Project.Status newStatus,
        Instant occurredAt
) implements DomainEvent {

    public ProjectStatusChangedEvent(Long projectId, String projectName, Project.Status newStatus) {
        this(projectId, projectName, newStatus, Instant.now());
    }
}
