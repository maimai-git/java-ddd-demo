package com.example.domain.shared.external;

import com.example.common.event.DomainEvent;

public interface EventPublisher {

    void publish(DomainEvent event);
}
