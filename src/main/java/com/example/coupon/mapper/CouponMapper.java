package com.example.coupon.mapper;

import com.example.coupon.dto.CouponResponse;
import com.example.coupon.entity.CouponEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponResponse toResponse(CouponEntity entity) {
        CouponResponse response = new CouponResponse();
        response.setId(entity.getId());
        response.setCode(entity.getCode());
        response.setDescription(entity.getDescription());
        response.setDiscountValue(entity.getDiscountValue());
        response.setExpirationDate(entity.getExpirationDate());
        response.setStatus(entity.getStatus());
        response.setPublished(entity.isPublished());
        response.setRedeemed(entity.isRedeemed());
        return response;
    }
}
