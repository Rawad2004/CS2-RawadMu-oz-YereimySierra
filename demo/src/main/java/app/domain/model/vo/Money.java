// File: src/main/java/app/domain/model/vo/Money.java
package app.domain.model.vo;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
public class Money implements Serializable {

    private BigDecimal amount;
    private String currency;

    protected Money() {
        // Constructor para JPA
    }

    public Money(BigDecimal amount) {
        this(amount, "COP");
    }

    public Money(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency != null ? currency : "COP";
    }

    public Money(String amount) {
        this(new BigDecimal(amount));
    }

    // MÉTODOS DE OPERACIONES MATEMÁTICAS
    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Subtraction result cannot be negative");
        }
        return new Money(result, this.currency);
    }

    public Money multiply(int multiplier) {
        return new Money(this.amount.multiply(new BigDecimal(multiplier)), this.currency);
    }

    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier), this.currency);
    }

    public Money divide(int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Division by zero");
        }
        return new Money(this.amount.divide(new BigDecimal(divisor), 2, RoundingMode.HALF_UP), this.currency);
    }

    // VALIDACIONES
    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isEqualTo(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) == 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    // MÉTODOS DE ACCESO
    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getFormattedAmount() {
        return String.format("$%,.2f %s", amount, currency);
    }

    // MÉTODOS ESTÁTICOS DE FÁBRICA
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money of(String amount) {
        return new Money(new BigDecimal(amount));
    }

    public static Money cop(double amount) {
        return new Money(BigDecimal.valueOf(amount), "COP");
    }

    // EQUALS Y HASHCODE
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return getFormattedAmount();
    }
}