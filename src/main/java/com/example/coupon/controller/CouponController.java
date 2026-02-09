package com.example.coupon.controller;

import com.example.coupon.dto.CouponRequest;
import com.example.coupon.dto.CouponResponse;
import com.example.coupon.entity.CouponEntity;
import com.example.coupon.mapper.CouponMapper;
import com.example.coupon.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService service;
    private final CouponMapper mapper;

    public CouponController(CouponService service, CouponMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Create a coupon",
            description = "Creates a new coupon. The coupon code is sanitized by removing special characters " +
                    "and must result in exactly 6 alphanumeric characters. The expiration date must not " +
                    "be in the past and the minimum discount value allowed is 0.5."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Coupon successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or business rule violation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Coupon code already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CouponResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Coupon creation request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CouponRequest.class))
            )
            @Valid @RequestBody CouponRequest request
    ) {
        CouponEntity coupon = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toResponse(coupon));
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get coupon by id",
            description = "Returns the coupon details by its UUID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coupon found"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<CouponResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                mapper.toResponse(service.getById(id))
        );
    }

    @GetMapping
    @Operation(
            summary = "List coupons",
            description = "Returns a paginated list of coupons ordered by expiration date in ascending order."
    )
    public ResponseEntity<Page<CouponResponse>> list(
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("expirationDate").ascending());
        Page<CouponResponse> response = service.listAll(pageable)
                .map(mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a coupon",
            description = "Performs a soft delete on a coupon. The coupon status is changed to DELETED. " +
                    "A coupon that is already deleted cannot be deleted again."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Coupon successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Coupon already deleted"),
            @ApiResponse(responseCode = "409", description = "Coupon not found")

    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
