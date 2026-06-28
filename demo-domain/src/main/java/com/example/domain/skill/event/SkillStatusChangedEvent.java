package com.example.domain.skill.event;

import com.example.common.event.DomainEvent;
import com.example.domain.skill.model.Skill;

import java.time.Instant;

public record SkillStatusChangedEvent(
        Long skillId,
        String skillName,
        Skill.Status newStatus,
        Instant occurredAt
) implements DomainEvent {

    public SkillStatusChangedEvent(Long skillId, String skillName, Skill.Status newStatus) {
        this(skillId, skillName, newStatus, Instant.now());
    }
}
