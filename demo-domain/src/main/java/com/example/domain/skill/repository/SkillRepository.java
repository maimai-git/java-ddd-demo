package com.example.domain.skill.repository;

import com.example.domain.skill.model.Skill;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SkillRepository {

    Skill save(Skill skill);

    Optional<Skill> findById(Long id);

    void deleteById(Long id);

    List<Skill> findByCategory(String category);

    /* ===== 精准字段更新 ===== */

    void updateName(Long id, String name, Instant updatedAt);

    void updateStatus(Long id, String status, Instant updatedAt);
}
