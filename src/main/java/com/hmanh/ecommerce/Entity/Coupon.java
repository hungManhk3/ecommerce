package com.hmanh.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private double discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private double minimumOrderValue;
    private boolean isActive = true;

    @ManyToMany(mappedBy = "usedCoupons")
    private Set<User> byUsers = new HashSet<>();
}
