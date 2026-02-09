package com.example.coupon.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Coupon status")
public enum CouponStatus {

    @Schema(description = "Coupon is active and can be used")
    ACTIVE,

    @Schema(description = "Coupon is inactive but not deleted")
    INACTIVE,

    @Schema(description = "Coupon has been soft deleted and cannot be used")
    DELETED
}

