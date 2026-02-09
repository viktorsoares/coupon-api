package com.example.coupon.repository;

import com.example.coupon.entity.CouponEntity;
import com.example.coupon.entity.CouponStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<CouponEntity, UUID> {

    Page<CouponEntity> findAllByStatus(CouponStatus status, Pageable pageable);

    Optional<CouponEntity> findByCode(String code);

}
