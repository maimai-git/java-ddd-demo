package com.example.domain.shared.model;

import com.example.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void shouldAddSameCurrency() {
        Money a = Money.rmb("10.00");
        Money b = Money.rmb("5.50");
        assertThat(a.add(b).amount()).isEqualByComparingTo("15.50");
    }

    @Test
    void shouldRejectDifferentCurrency() {
        Money rmb = Money.rmb("10");
        Money usd = new Money(BigDecimal.TEN, Currency.getInstance("USD"));
        assertThatThrownBy(() -> rmb.add(usd))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("币种不匹配");
    }

    @Test
    void shouldMultiply() {
        assertThat(Money.rmb("3.5").multiply(4).amount()).isEqualByComparingTo("14.00");
    }

    @Test
    void shouldSubtract() {
        assertThat(Money.rmb("10").subtract(Money.rmb("3")).amount())
                .isEqualByComparingTo("7");
    }

    @Test
    void zeroShouldReturnZeroAmount() {
        Money zero = Money.zero(Currency.getInstance("CNY"));
        assertThat(zero.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldRejectSubtractDifferentCurrency() {
        Money rmb = Money.rmb("10");
        Money usd = new Money(BigDecimal.ONE, Currency.getInstance("USD"));
        assertThatThrownBy(() -> rmb.subtract(usd))
                .isInstanceOf(BizException.class);
    }
}
