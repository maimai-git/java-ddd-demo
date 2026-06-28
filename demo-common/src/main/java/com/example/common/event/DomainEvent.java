package com.example.common.event;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredAt();
}
