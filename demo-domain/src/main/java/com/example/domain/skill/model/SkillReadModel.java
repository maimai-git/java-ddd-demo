package com.example.domain.skill.model;

import java.time.Instant;

/** CQRS读投影 —— 轻量数据传输对象，不承载领域行为 */
public record SkillReadModel(
        Long id,
        String name,
        String category,
        String level,
        String description,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}
