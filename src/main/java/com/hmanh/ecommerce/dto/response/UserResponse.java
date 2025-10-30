package com.hmanh.ecommerce.dto.response;

import com.hmanh.ecommerce.Entity.Coupon;
import com.hmanh.ecommerce.domain.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private USER_ROLE role;
    private String mobile;
    private Set<AddressResponse> addresses;
    private Set<CouponResponse> coupons;

}
