package net.vitic.eap.a.domain.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Money {

    private BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money dollars(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money dollars(int amount) {
        return new Money(new BigDecimal(amount));
    }

    public Money add(Money money) {
        this.amount = amount.add(money.amount());
        return this;
    }

    public BigDecimal amount() {
        return this.amount;
    }

    public Money[] allocate(int size) {
        BigDecimal instalment = this.amount.divide(new BigDecimal(size),
                                                   new MathContext(1, RoundingMode.HALF_EVEN));

        Money[] allocations = new Money[size];
        for (int i = 0; i < size; i++) {
            allocations[i] = new Money(instalment);
        }
        return allocations;
    }
}
