package com.example.application.common;

import com.example.common.event.DomainEvent;
import com.example.domain.shared.external.EventPublisher;
import com.example.domain.shared.model.BaseEntity;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.function.Consumer;

/**
 * 领域事件工具 — 确保事务提交后才发布，并提供"持久化 + 发布"的复用模板。
 */
public final class DomainEvents {

    private DomainEvents() {}

    /**
     * 事务提交后发布事件。
     */
    public static void publishAfterCommit(EventPublisher publisher, List<DomainEvent> events) {
        if (events.isEmpty()) return;
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        events.forEach(publisher::publish);
                    }
                });
    }

    /**
     * 保存实体并发布其收集的领域事件——"改状态→存→发"三步合一。
     * <pre>
     * skill.activate();
     * DomainEvents.saveAndPublish(skill, skillDomainService::save, eventPublisher);
     * </pre>
     */
    public static <T extends BaseEntity> void saveAndPublish(
            T entity, Consumer<T> save, EventPublisher publisher) {
        List<DomainEvent> events = entity.getDomainEvents();
        save.accept(entity);
        publishAfterCommit(publisher, events);
    }
}
