package com.hmanh.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long id;
    private UserResponse user;
    private Set<CartItemResponse> cartItems;
    private int totalSellingPrice;
    private int totalQuantity;
    private int totalMrpPrice;
    private int discount;
    private String couponCode;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
