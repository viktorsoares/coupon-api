package com.example.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Coupon creation request")
public class CouponRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0.5")
    private BigDecimal discountValue;

    @NotNull
    private LocalDateTime expirationDate;

    private boolean published = false;

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

    public void setPublished(boolean published) {
        this.published = published;
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

    public boolean isPublished() {
        return published;
    }

}
