package com.example.coupon.dto;

import com.example.coupon.entity.CouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CouponResponse {

    @Schema(description = "Unique identifier of the coupon", example = "c0a80123-45ab-6789-0abc-def123456789")
    private UUID id;

    @Schema(description = "Coupon code", example = "ABY123")
    private String code;

    @Schema(description = "Coupon description", example = "10% off on next purchase")
    private String description;

    @Schema(description = "Discount value of the coupon", example = "10.5")
    private BigDecimal discountValue;

    @Schema(description = "Expiration date and time of the coupon", example = "2026-12-31T23:59:59")
    private LocalDateTime expirationDate;

    @Schema(description = "Current status of the coupon", example = "ACTIVE")
    private CouponStatus status;

    @Schema(description = "Whether the coupon is published", example = "true")
    private boolean published;

    @Schema(description = "Whether the coupon has been redeemed", example = "false")
    private boolean redeemed;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
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
