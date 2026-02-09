package com.example.coupon.entity;

import com.example.coupon.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
public class CouponEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 6, unique = true)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;

    protected CouponEntity() {}

    public static CouponEntity create(String code, String description, BigDecimal discountValue,
                                      LocalDateTime expirationDate, boolean published) {
        CouponEntity coupon = new CouponEntity();
        coupon.code = code;
        coupon.description = description;
        coupon.discountValue = discountValue;
        coupon.expirationDate = expirationDate;
        coupon.published = published;
        coupon.redeemed = false;
        coupon.status = CouponStatus.ACTIVE;

        coupon.validateForCreation();
        return coupon;
    }

    public void validateForCreation() {
        validateCode();
        validateDiscount();
        validateExpirationDate();
    }

    private void validateCode() {
        if (code == null || code.length() != 6 || !code.matches("[A-Z0-9]{6}")) {
            throw new BusinessException("Coupon code must have exactly 6 alphanumeric");
        }
    }

    private void validateDiscount() {
        if (discountValue == null || discountValue.compareTo(BigDecimal.valueOf(0.5)) < 0) {
            throw new BusinessException("Minimum discount value is 0.5");
        }
    }

    private void validateExpirationDate() {
        if (expirationDate == null || expirationDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Expiration date cannot be in the past");
        }
    }

    public void markAsDeleted() {
        if (this.status == CouponStatus.DELETED) {
            throw new BusinessException("Coupon already deleted");
        }
        this.status = CouponStatus.DELETED;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public boolean isPublished() {
        return published;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

}
