package com.example.coupon.service;

import com.example.coupon.dto.CouponRequest;
import com.example.coupon.entity.CouponEntity;
import com.example.coupon.entity.CouponStatus;
import com.example.coupon.exception.BusinessException;
import com.example.coupon.repository.CouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CouponService {

    private final CouponRepository repository;

    public CouponService(CouponRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CouponEntity create(CouponRequest request) {
        String sanitizedCode = request.getCode().replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        repository.findByCode(sanitizedCode)
                .ifPresent(c -> { throw new BusinessException("Coupon code already exists"); });

        CouponEntity coupon = CouponEntity.create(
                sanitizedCode,
                request.getDescription(),
                request.getDiscountValue(),
                request.getExpirationDate(),
                request.isPublished()
        );

        return repository.save(coupon);
    }

    @Transactional
    public void delete(UUID id) {
        CouponEntity coupon = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Coupon not found"));

        coupon.markAsDeleted();
        repository.save(coupon);
    }

    @Transactional(readOnly = true)
    public CouponEntity getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Coupon not found"));
    }

    @Transactional(readOnly = true)
    public Page<CouponEntity> listAll(Pageable pageable) {
        return repository.findAllByStatus(CouponStatus.ACTIVE, pageable);
    }
}
