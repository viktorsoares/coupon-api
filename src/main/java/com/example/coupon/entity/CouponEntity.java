package com.example.coupon.entity;

import com.example.coupon.value.CouponCode;
import com.example.coupon.value.DiscountValue;
import com.example.coupon.value.ExpirationDate;
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

    public static CouponEntity create(String rawCode, String description, BigDecimal discount, LocalDateTime expiration, boolean published) {
        CouponCode code = new CouponCode(rawCode);
        DiscountValue discountValue = new DiscountValue(discount);
        ExpirationDate expirationDate = new ExpirationDate(expiration);

        CouponEntity entity = new CouponEntity();
        entity.code = code.getCode();
        entity.description = description;
        entity.discountValue = discountValue.getValue();
        entity.expirationDate = expirationDate.getExpiration();
        entity.status = CouponStatus.ACTIVE;
        entity.published = published;
        entity.redeemed = false;
        return entity;
    }

    public void markAsDeleted() {
        if (this.status == CouponStatus.DELETED) {
            throw new com.example.coupon.exception.BusinessException("Coupon already deleted");
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
