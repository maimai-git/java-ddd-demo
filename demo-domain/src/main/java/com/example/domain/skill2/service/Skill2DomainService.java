package com.example.domain.skill2.service;

import com.example.domain.skill2.model.Skill2Entity;
import com.example.domain.skill2.model.Skill2Entity.Level;
import com.example.domain.skill2.model.Skill2Entity.Status;

import java.util.List;
import java.util.Optional;

/**
 * Style 2 领域服务 — 接口定义在 domain，实现在 domain。
 *
 * <p>对比 {@code com.example.domain.skill.service.SkillDomainService}：
 * <ul>
 *   <li>SkillDomainService: 具体类（无接口）</li>
 *   <li>Skill2DomainService: 接口 + impl，方便测试 mock 和扩展</li>
 * </ul>
 */
public interface Skill2DomainService {

    Optional<Skill2Entity> findById(Long id);

    List<Skill2Entity> findAll();

    Skill2Entity create(String name, String category, Level level, String description);

    Skill2Entity changeName(Long id, String newName);

    Skill2Entity changeStatus(Long id, Status newStatus);

    void delete(Long id);
}
