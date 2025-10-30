package com.hmanh.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private Long id;
    private String code;
    private double discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private double minimumOrderValue;
    private boolean active;
    private boolean expired;
    private boolean usable;
}