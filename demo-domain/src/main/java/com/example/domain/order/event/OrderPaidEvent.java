package com.example.domain.order.event;

import com.example.common.event.DomainEvent;
import com.example.domain.shared.model.Money;

import java.time.Instant;

public record OrderPaidEvent(String orderNo, Money amount, Instant occurredAt) implements DomainEvent {
}
