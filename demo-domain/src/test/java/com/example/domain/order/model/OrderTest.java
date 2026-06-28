package com.example.domain.order.model;

import com.example.common.event.DomainEvent;
import com.example.common.exception.BizException;
import com.example.domain.order.event.OrderPaidEvent;
import com.example.domain.shared.model.Address;
import com.example.domain.shared.model.Money;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private static final Address address = new Address("广东", "深圳", "南山", "科技园");

    @Test
    void shouldPlaceOrderWithInitialState() {
        Order order = Order.place("ORD-001", address);
        assertThat(order.getOrderNo()).isEqualTo("ORD-001");
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.NEW);
        assertThat(order.getItems()).isEmpty();
    }

    @Test
    void shouldRejectEmptyOrderNo() {
        assertThatThrownBy(() -> Order.place("", address))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("订单号不能为空");
    }

    @Test
    void shouldCalculateTotalAmount() {
        Order order = Order.place("ORD-002", address);
        order.addItem(OrderItem.of(1L, "商品A", Money.rmb("10.00"), 2));
        order.addItem(OrderItem.of(2L, "商品B", Money.rmb("5.00"), 3));
        assertThat(order.totalAmount().amount()).isEqualByComparingTo("35.00");
    }

    @Test
    void shouldPayWhenNew() {
        Order order = Order.place("ORD-003", address);
        order.addItem(OrderItem.of(1L, "商品", Money.rmb("100"), 1));
        order.pay();
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.PAID);
    }

    @Test
    void shouldPublishEventOnPay() {
        Order order = Order.place("ORD-004", address);
        order.addItem(OrderItem.of(1L, "商品", Money.rmb("50"), 2));
        order.pay();
        List<DomainEvent> events = order.getDomainEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(OrderPaidEvent.class);
        OrderPaidEvent event = (OrderPaidEvent) events.get(0);
        assertThat(event.orderNo()).isEqualTo("ORD-004");
        assertThat(event.amount().amount()).isEqualByComparingTo("100.00");
    }

    @Test
    void shouldRejectPayWhenAlreadyPaid() {
        Order order = Order.place("ORD-005", address);
        order.pay();
        assertThatThrownBy(order::pay)
                .isInstanceOf(BizException.class);
    }

    @Test
    void shouldClearEventsAfterGet() {
        Order order = Order.place("ORD-006", address);
        order.pay();
        assertThat(order.getDomainEvents()).hasSize(1);
        assertThat(order.getDomainEvents()).isEmpty(); // 第二次调用已清空
    }

    @Test
    void zeroItemsShouldReturnZeroTotal() {
        Order order = Order.place("ORD-007", address);
        assertThat(order.totalAmount().amount()).isEqualByComparingTo("0");
    }
}
