package com.example.domain.shared.model;

import com.example.common.event.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 审计字段基类 — 所有聚合根和实体继承它，减少重复。
 */
public abstract class BaseEntity {

    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /** 聚合根行为方法内部调用，收集需要发布的领域事件 */
    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    /** 持久化后调用，获取并清空事件。在事务提交后发布以保证一致性。 */
    public List<DomainEvent> getDomainEvents() {
        if (domainEvents.isEmpty()) return Collections.emptyList();
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
