package com.example.domain.skill.repository;

import com.example.domain.skill.model.SkillFilter;
import com.example.domain.skill.model.SkillReadModel;

import java.util.List;
import java.util.Optional;

/**
 * CQRS 读模型 — 只读查询不走领域模型，直接返回投影。
 * 写操作仍走 SkillRepository（领域仓储 + 聚合根）。
 */
public interface SkillReadRepository {

    Optional<SkillReadModel> findById(Long id);

    List<SkillReadModel> findPaged(SkillFilter filter, int offset, int limit);

    long count(SkillFilter filter);
}
