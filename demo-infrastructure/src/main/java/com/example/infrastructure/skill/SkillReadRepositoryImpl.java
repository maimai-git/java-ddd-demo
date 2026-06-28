package com.example.infrastructure.skill;

import com.example.domain.skill.model.SkillFilter;
import com.example.domain.skill.model.SkillReadModel;
import com.example.domain.skill.repository.SkillReadRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SkillReadRepositoryImpl implements SkillReadRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<SkillReadModel> findById(Long id) {
        return Optional.ofNullable(em.find(SkillPO.class, id))
                .map(SkillReadConverter::toReadModel);
    }

    @Override
    public List<SkillReadModel> findPaged(SkillFilter filter, int offset, int limit) {
        var pair = buildWhere(filter);
        String jpql = "SELECT s FROM SkillPO s" + pair.where + " ORDER BY s.id";
        TypedQuery<SkillPO> query = em.createQuery(jpql, SkillPO.class);
        for (int i = 0; i < pair.params.size(); i++)
            query.setParameter("p" + i, pair.params.get(i));
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList().stream().map(SkillReadConverter::toReadModel).toList();
    }

    @Override
    public long count(SkillFilter filter) {
        var pair = buildWhere(filter);
        String jpql = "SELECT COUNT(s) FROM SkillPO s" + pair.where;
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        for (int i = 0; i < pair.params.size(); i++)
            query.setParameter("p" + i, pair.params.get(i));
        return query.getSingleResult();
    }

    private WherePair buildWhere(SkillFilter f) {
        List<String> clauses = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (f.keyword() != null && !f.keyword().isBlank()) {
            clauses.add("(s.name LIKE :p0 OR s.category LIKE :p0)");
            params.add("%" + f.keyword() + "%");
        }
        if (f.category() != null) {
            clauses.add("s.category = :p" + params.size());
            params.add(f.category());
        }
        if (f.level() != null) {
            clauses.add("s.level = :p" + params.size());
            params.add(f.level().name());
        }
        if (f.status() != null) {
            clauses.add("s.status = :p" + params.size());
            params.add(f.status().name());
        }

        if (clauses.isEmpty()) return new WherePair("", List.of());
        return new WherePair(" WHERE " + String.join(" AND ", clauses), params);
    }

    private record WherePair(String where, List<Object> params) {}
}
