package com.example.domain.skill2.service.impl;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import com.example.domain.skill2.model.Skill2Entity;
import com.example.domain.skill2.model.Skill2Entity.Level;
import com.example.domain.skill2.model.Skill2Entity.Status;
import com.example.domain.skill2.repository.Skill2Repository;
import com.example.domain.skill2.service.Skill2DomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 风格二领域服务 —— 事务在 DomainService 层，不在 AppService。
 *
 * <p>设计理由：
 * <ul>
 *   <li>领域服务本身就包含多步仓储操作（查 + 改 + 存），</li>
 *       事务边界和领域逻辑天然一致，放在同一层更内聚。</li>
 *   <li>AppService 只负责用例编排 + DTO 映射，不关心事务。</li>
 * </ul>
 *
 * <p>和风格一 {@code SkillDomainService} 的差异：
 * <ul>
 *   <li>风格一：具体类，无接口，@Transactional 在 AppService 层。</li>
 *   <li>风格二：接口 + 实现，@Transactional 在 DomainService 层。</li>
 * </ul>
 */
@Service
@Transactional
public class Skill2DomainServiceImpl implements Skill2DomainService {

    private final Skill2Repository repository;

    public Skill2DomainServiceImpl(Skill2Repository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true) // 只读事务，数据库可做读写分离路由
    public Optional<Skill2Entity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Skill2Entity> findAll() {
        return repository.findAll();
    }

    @Override
    public Skill2Entity create(String name, String category, Level level, String description) {
        Skill2Entity entity = Skill2Entity.create(name, category, level, description);
        return repository.save(entity); // save 返回持久化后的实体（包含数据库生成的 id）
    }

    @Override
    public Skill2Entity changeName(Long id, String newName) {
        // 1. 查出聚合根
        Skill2Entity entity = repository.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        // 2. 调行为方法 —— 校验 + 更新字段
        entity.changeName(newName);
        // 3. 精准字段更新 —— 只有 UPDATE SET name = ? WHERE id = ?，不碰其他列
        repository.updateName(id, entity.getName());
        return entity;
    }

    @Override
    public Skill2Entity changeStatus(Long id, Status newStatus) {
        Skill2Entity entity = repository.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        entity.changeStatus(newStatus);
        // @Modifying JPQL UPDATE —— 只改 status 列
        repository.updateStatus(id, entity.getStatus());
        return entity;
    }

    @Override
    public void delete(Long id) {
        // ★ 先查再删 —— 保证实体存在才删，不存在抛异常
        //    传 Entity 而非 id，防止删不存在的记录时静默失败
        Skill2Entity entity = repository.findById(id).orElseThrow(() -> new BizException(ErrorCode.SKILL_NOT_FOUND));
        repository.delete(entity);
    }
}
