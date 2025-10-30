package com.hmanh.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private ProductResponse product;
    private String size;
    private int quantity;
    private Integer mrpPrice;
    private Integer sellingPrice;
    private Integer totalMrpPrice;    // mrpPrice * quantity
    private Integer totalSellingPrice; // sellingPrice * quantity
    private Integer discount;          // totalMrpPrice - totalSellingPrice / totalMrpPrice * 100
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}