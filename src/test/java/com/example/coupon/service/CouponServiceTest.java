package com.example.coupon.service;

import com.example.coupon.dto.CouponRequest;
import com.example.coupon.entity.CouponEntity;
import com.example.coupon.entity.CouponStatus;
import com.example.coupon.exception.BusinessException;
import com.example.coupon.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository repository;

    @InjectMocks
    private CouponService service;

    @Test
    void shouldCreateCouponSuccessfully() {
        CouponRequest request = validRequest("AB1C2X");

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CouponEntity coupon = service.create(request);

        assertNotNull(coupon);
        assertEquals("AB1C2X", coupon.getCode());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
        assertFalse(coupon.isRedeemed());
    }

    @Test
    void shouldFailWhenCouponCodeHasInvalidSize() {
        CouponRequest request = validRequest("ABC");

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        assertEquals("Coupon code must have exactly 6 alphanumeric", ex.getMessage());
    }

    @Test
    void shouldFailWhenDiscountValueIsBelowMinimum() {
        CouponRequest request = validRequest("ABC123");
        request.setDiscountValue(BigDecimal.valueOf(0.3));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        assertEquals("Minimum discount value is 0.5", ex.getMessage());
    }

    @Test
    void shouldAllowMinimumDiscountValue() {
        CouponRequest request = validRequest("ABC123");
        request.setDiscountValue(BigDecimal.valueOf(0.5));

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CouponEntity coupon = service.create(request);

        assertEquals(BigDecimal.valueOf(0.5), coupon.getDiscountValue());
    }

    @Test
    void shouldFailWhenExpirationDateIsInPast() {
        CouponRequest request = validRequest("ABC123");
        request.setExpirationDate(LocalDateTime.now().minusDays(1));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        assertEquals("Expiration date cannot be in the past", ex.getMessage());
    }

    @Test
    void shouldFailWhenDuplicateCouponCode() {
        CouponRequest request = validRequest("DUPLIC");

        CouponEntity existing = CouponEntity.create(
                "DUPLIC",
                "Existing coupon",
                BigDecimal.valueOf(0.8),
                LocalDateTime.now().plusDays(5),
                false
        );

        when(repository.findByCode("DUPLIC")).thenReturn(Optional.of(existing));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        assertEquals("Coupon code already exists", ex.getMessage());
    }

    @Test
    void shouldSoftDeleteCoupon() {
        UUID id = UUID.randomUUID();
        CouponEntity coupon = CouponEntity.create(
                "DELETE",
                "To delete",
                BigDecimal.valueOf(0.8),
                LocalDateTime.now().plusDays(5),
                false
        );

        coupon.getClass().getDeclaredFields();

        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        service.delete(id);

        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        verify(repository).save(coupon);
    }

    @Test
    void shouldFailWhenDeletingAlreadyDeletedCoupon() {
        UUID id = UUID.randomUUID();
        CouponEntity coupon = CouponEntity.create(
                "DELDED",
                "Already deleted",
                BigDecimal.valueOf(0.8),
                LocalDateTime.now().plusDays(5),
                false
        );
        coupon.markAsDeleted();

        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.delete(id)
        );

        assertEquals("Coupon already deleted", ex.getMessage());
    }

    @Test
    void shouldFailWhenDeletingNonExistentCoupon() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.delete(id)
        );

        assertEquals("Coupon not found", ex.getMessage());
    }

    @Test
    void shouldGetCouponByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        CouponEntity coupon = CouponEntity.create(
                "GET001",
                "Get by ID",
                BigDecimal.valueOf(0.8),
                LocalDateTime.now().plusDays(5),
                false
        );

        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        CouponEntity result = service.getById(id);

        assertNotNull(result);
        assertEquals(coupon.getCode(), result.getCode());
    }

    @Test
    void shouldFailWhenCouponNotFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.getById(id)
        );

        assertEquals("Coupon not found", ex.getMessage());
    }

    @Test
    void shouldListOnlyActiveCoupons() {
        Pageable pageable = PageRequest.of(0, 10);
        CouponEntity activeCoupon = CouponEntity.create(
                "ACTIVE",
                "Active coupon",
                BigDecimal.valueOf(0.8),
                LocalDateTime.now().plusDays(5),
                false
        );

        Page<CouponEntity> page = new PageImpl<>(List.of(activeCoupon));
        when(repository.findAllByStatus(CouponStatus.ACTIVE, pageable)).thenReturn(page);

        Page<CouponEntity> result = service.listAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(CouponStatus.ACTIVE, result.getContent().get(0).getStatus());
    }

    private CouponRequest validRequest(String code) {
        CouponRequest request = new CouponRequest();
        request.setCode(code);
        request.setDescription("Valid description");
        request.setDiscountValue(BigDecimal.valueOf(0.8));
        request.setExpirationDate(LocalDateTime.now().plusDays(10));
        request.setPublished(false);
        return request;
    }
}
