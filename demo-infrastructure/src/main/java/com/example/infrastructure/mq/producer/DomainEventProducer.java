package com.example.infrastructure.mq.producer;

import com.example.common.event.DomainEvent;
import com.example.domain.order.event.OrderPaidEvent;
import com.example.domain.project.event.ProjectStatusChangedEvent;
import com.example.domain.skill.event.SkillStatusChangedEvent;
import com.example.domain.shared.external.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DomainEventProducer implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DomainEventProducer.class);

    @Override
    public void publish(DomainEvent event) {
        switch (event) {
            case OrderPaidEvent paid ->
                    log.info("MQ send: OrderPaid — orderNo={}, amount={}", paid.orderNo(), paid.amount());
            case SkillStatusChangedEvent changed ->
                    log.info("MQ send: SkillStatusChanged — skillId={}, name={}, newStatus={}",
                            changed.skillId(), changed.skillName(), changed.newStatus());
            case ProjectStatusChangedEvent changed ->
                    log.info("MQ send: ProjectStatusChanged — projectId={}, name={}, newStatus={}",
                            changed.projectId(), changed.projectName(), changed.newStatus());
            default ->
                    log.info("MQ send: event={}", event.getClass().getSimpleName());
        }
    }
}
