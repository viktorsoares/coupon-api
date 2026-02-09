package com.example.coupon.value;

import com.example.coupon.exception.BusinessException;

import java.math.BigDecimal;
import java.util.Objects;

public final class DiscountValue {

    private final BigDecimal value;

    public DiscountValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.valueOf(0.5)) < 0) {
            throw new BusinessException("Minimum discount value is 0.5");
        }
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountValue)) return false;
        DiscountValue that = (DiscountValue) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
