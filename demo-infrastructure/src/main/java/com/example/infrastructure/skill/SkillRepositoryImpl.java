package com.example.infrastructure.skill;

import com.example.domain.skill.model.Skill;
import com.example.domain.skill.repository.SkillRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class SkillRepositoryImpl implements SkillRepository {

    private final SkillJpaRepository jpaRepository;

    @PersistenceContext
    private EntityManager em;

    public SkillRepositoryImpl(SkillJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Skill save(Skill skill) {
        var po = SkillConverter.toPO(skill);
        var saved = jpaRepository.save(po);
        return SkillConverter.toDomain(saved);
    }

    @Override
    public Optional<Skill> findById(Long id) {
        return jpaRepository.findById(id).map(SkillConverter::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Skill> findByCategory(String category) {
        return jpaRepository.findAll().stream().filter(po -> category.equals(po.getCategory())).map(SkillConverter::toDomain).toList();
    }

    /* ===== 精准字段更新 — 手写 SQL，只改目标列 ===== */

    @Override
    public void updateName(Long id, String name, Instant updatedAt) {
        em.createNativeQuery("UPDATE t_skill SET name = :name, updated_at = :now WHERE id = :id")
                .setParameter("name", name)
                .setParameter("now", updatedAt)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void updateStatus(Long id, String status, Instant updatedAt) {
        em.createNativeQuery("UPDATE t_skill SET status = :status, updated_at = :now WHERE id = :id")
                .setParameter("status", status)
                .setParameter("now", updatedAt)
                .setParameter("id", id)
                .executeUpdate();
    }
}
