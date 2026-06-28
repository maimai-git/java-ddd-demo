package com.example.infrastructure.skill2;

import com.example.domain.skill2.repository.Skill2Projection;

/**
 * JPQL 构造器表达式 {@code new Skill2ProjectionImpl(...)} 的目标类。
 * 包级私有，不暴露给外部。
 */
class Skill2ProjectionImpl implements Skill2Projection {

    private final Long id;
    private final String name;
    private final String category;
    private final String status;

    public Skill2ProjectionImpl(Long id, String name, String category, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
    }

    @Override public Long getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getCategory() { return category; }
    @Override public String getStatus() { return status; }
}
