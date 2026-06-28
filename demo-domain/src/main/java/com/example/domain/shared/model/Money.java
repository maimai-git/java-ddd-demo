package com.example.domain.shared.model;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
    }

    public static Money rmb(String amount) {
        return new Money(new BigDecimal(amount), Currency.getInstance("CNY"));
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new BizException(ErrorCode.CURRENCY_MISMATCH);
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new BizException(ErrorCode.CURRENCY_MISMATCH);
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public int compareTo(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new BizException(ErrorCode.CURRENCY_MISMATCH);
        }
        return this.amount.compareTo(other.amount);
    }
}
