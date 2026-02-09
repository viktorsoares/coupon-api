package com.example.coupon.value;

import com.example.coupon.exception.BusinessException;

import java.util.Objects;

public final class CouponCode {

    private final String code;

    public CouponCode(String code) {
        if (code == null) {
            throw new BusinessException("Coupon code cannot be null");
        }
        // Remove caracteres especiais e transforma em maiúsculo
        String sanitized = code.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (sanitized.length() != 6) {
            throw new BusinessException("Coupon code must have exactly 6 alphanumeric characters");
        }

        this.code = sanitized;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CouponCode)) return false;
        CouponCode that = (CouponCode) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
