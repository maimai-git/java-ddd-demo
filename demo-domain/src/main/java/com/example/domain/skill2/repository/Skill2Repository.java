package com.example.domain.skill2.repository;

import com.example.domain.skill2.model.Skill2Entity;
import com.example.domain.skill2.model.Skill2Entity.Status;

import java.util.List;
import java.util.Optional;

/**
 * Style 2 仓储接口 — 纯接口，定义在 domain，实现在 infra。
 *
 * <p>对比 {@code com.example.domain.skill.repository.SkillRepository}：
 * <ul>
 *   <li>SkillRepository: 返回领域模型 Skill，impl 手写 EntityManager 查询</li>
 *   <li>Skill2Repository: 返回 JPA Entity，impl 靠 Spring Data JPA 自动生成</li>
 * </ul>
 */
public interface Skill2Repository {

    Skill2Entity save(Skill2Entity entity);

    Optional<Skill2Entity> findById(Long id);

    List<Skill2Entity> findAll();

    List<Skill2Entity> findByCategory(String category);

    /** delete 接收 Entity（而非 id），保证实体已加载再删除 */
    void delete(Skill2Entity entity);

    // ── 精准字段更新（@Modifying JPQL，不查全量再 save）──

    int updateName(Long id, String name);

    int updateStatus(Long id, Status status);
}
