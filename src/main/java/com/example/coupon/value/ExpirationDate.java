package com.example.coupon.value;

import com.example.coupon.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ExpirationDate {

    private final LocalDateTime expiration;

    public ExpirationDate(LocalDateTime expiration) {
        if (expiration == null || expiration.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Expiration date cannot be in the past");
        }
        this.expiration = expiration;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpirationDate)) return false;
        ExpirationDate that = (ExpirationDate) o;
        return expiration.equals(that.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiration);
    }
}
