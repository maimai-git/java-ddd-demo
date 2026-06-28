package com.example.infrastructure.skill2;

import com.example.domain.skill2.repository.Skill2Projection;
import com.example.domain.skill2.repository.Skill2QueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CQRS 读仓库实现 —— 用 JPQL 构造器表达式直接映射到投影接口。
 *
 * <p>和风格一 {@code SkillReadRepositoryImpl} 的对比：
 * <ul>
 *   <li>风格一：EntityManager.createQuery → 返回 List&lt;SkillPO&gt;
 *       → stream().map(SkillReadConverter::toReadModel) → List&lt;SkillReadModel&gt;。</li>
 *   <li>风格二：EntityManager.createQuery → JPQL {@code SELECT new Skill2ProjectionImpl(...)}
 *       → 一句映射，不需要中间步骤。</li>
 * </ul>
 *
 * <p><b>JPQL 构造器表达式原理：</b>
 * {@code SELECT new 全限定类名(字段1, 字段2, ...) FROM Entity}
 * JPA 会为每一行调用那个类的构造器，直接产出投影对象。
 * 不会触发完整的 Entity 加载（不读不需要的列），性能更好。
 */
@Repository
public class Skill2QueryJpaRepository implements Skill2QueryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Skill2Projection> findAllSummary() {
        // JPQL 构造器表达式 —— 只查 4 个字段，不加载完整 Entity
        return cast(em.createQuery("""
                SELECT new com.example.infrastructure.skill2.Skill2ProjectionImpl(s.id, s.name, s.category, s.status)
                FROM Skill2Entity s ORDER BY s.id DESC
                """, Skill2ProjectionImpl.class).getResultList());
    }

    @Override
    public List<Skill2Projection> search(String keyword, String category) {
        return cast(em.createQuery("""
                SELECT new com.example.infrastructure.skill2.Skill2ProjectionImpl(s.id, s.name, s.category, s.status)
                FROM Skill2Entity s
                WHERE s.category = :cat AND (s.name LIKE :kw OR s.description LIKE :kw)
                ORDER BY s.id DESC
                """, Skill2ProjectionImpl.class)
                .setParameter("cat", category)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList());
    }

    /**
     * 绕过泛型检查。
     * JPA 构造器表达式返回的是具体实现类 Skill2ProjectionImpl，
     * 但泛型声明为 List&lt;Skill2Projection&gt;，编译期不兼容。
     * 实际运行时 List&lt;Skill2ProjectionImpl&gt; 和 List&lt;Skill2Projection&gt; 完全一样。
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> cast(List<?> list) {
        return (List<T>) list;
    }
}
