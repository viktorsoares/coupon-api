package com.example.coupon.integration;

import com.example.coupon.dto.CouponRequest;
import com.example.coupon.dto.CouponResponse;
import com.example.coupon.entity.CouponEntity;
import com.example.coupon.entity.CouponStatus;
import com.example.coupon.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CouponRepository repository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/coupon";
        repository.deleteAll();
    }

    @Test
    void shouldCreateCouponSuccessfully() {
        CouponRequest request = new CouponRequest();
        request.setCode("AB1C2D");
        request.setDescription("10% off");
        request.setDiscountValue(BigDecimal.valueOf(1.0));
        request.setExpirationDate(LocalDateTime.now().plusDays(5));
        request.setPublished(false);

        ResponseEntity<CouponResponse> response =
                restTemplate.postForEntity(baseUrl, request, CouponResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        CouponResponse coupon = response.getBody();
        assertNotNull(coupon);
        assertEquals("AB1C2D", coupon.getCode());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
    }

    @Test
    void shouldFailCreateCouponWithInvalidCode() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setCode("A#B"); // inválido, menos de 6
        request.setDescription("Desc");
        request.setDiscountValue(BigDecimal.valueOf(1.0));
        request.setExpirationDate(LocalDateTime.now().plusDays(5));
        request.setPublished(false);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Coupon code must have exactly 6 alphanumeric"));
    }

    @Test
    void shouldFailCreateCouponWithPastExpiration() {
        CouponRequest request = new CouponRequest();
        request.setCode("ABC123");
        request.setDescription("Desc");
        request.setDiscountValue(BigDecimal.valueOf(1.0));
        request.setExpirationDate(LocalDateTime.now().minusDays(1));
        request.setPublished(false);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Expiration date cannot be in the past"));
    }

    @Test
    void shouldGetCouponById() {
        CouponEntity saved = repository.save(CouponEntity.create(
                "ABC123",
                "Desc",
                BigDecimal.valueOf(1.0),
                LocalDateTime.now().plusDays(5),
                false
        ));

        ResponseEntity<CouponResponse> response = restTemplate.getForEntity(
                baseUrl + "/" + saved.getId(), CouponResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saved.getId(), response.getBody().getId());
    }

    @Test
    void shouldSoftDeleteCoupon() {
        CouponEntity saved = repository.save(CouponEntity.create(
                "ABC123",
                "Desc",
                BigDecimal.valueOf(1.0),
                LocalDateTime.now().plusDays(5),
                false
        ));

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + saved.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(new HttpHeaders()),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        CouponEntity deleted = repository.findById(saved.getId()).orElseThrow();
        assertEquals(CouponStatus.DELETED, deleted.getStatus());
    }

    @Test
    void shouldFailToDeleteAlreadyDeletedCoupon() {
        CouponEntity saved = repository.save(CouponEntity.create(
                "ABC123",
                "Desc",
                BigDecimal.valueOf(1.0),
                LocalDateTime.now().plusDays(5),
                false
        ));
        saved.markAsDeleted();
        repository.save(saved);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + saved.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(new HttpHeaders()),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Coupon already deleted"));
    }

    @Test
    void shouldListCoupons() {
        repository.save(CouponEntity.create(
                "ABC123",
                "Desc 1",
                BigDecimal.valueOf(1.0),
                LocalDateTime.now().plusDays(5),
                false
        ));
        repository.save(CouponEntity.create(
                "DEF456",
                "Desc 2",
                BigDecimal.valueOf(2.0),
                LocalDateTime.now().plusDays(3),
                false
        ));

        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "?page=0&size=10",
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("ABC123"));
        assertTrue(response.getBody().contains("DEF456"));
    }
}
