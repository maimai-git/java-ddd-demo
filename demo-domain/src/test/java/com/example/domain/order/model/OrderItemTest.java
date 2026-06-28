package com.example.domain.order.model;

import com.example.domain.shared.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    void shouldCalculateTotalPrice() {
        OrderItem item = OrderItem.of(1L, "商品", Money.rmb("10.00"), 3);
        assertThat(item.totalPrice().amount()).isEqualByComparingTo("30.00");
    }

    @Test
    void shouldHandleSingleQuantity() {
        OrderItem item = OrderItem.of(2L, "单品", Money.rmb("99.99"), 1);
        assertThat(item.totalPrice().amount()).isEqualByComparingTo("99.99");
    }

    @Test
    void shouldReconstituteWithId() {
        OrderItem item = OrderItem.reconstitute(100L, 1L, "商品", Money.rmb("50.00"), 2);
        assertThat(item.getId()).isEqualTo(100L);
        assertThat(item.getProductName()).isEqualTo("商品");
    }
}
