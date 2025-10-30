package com.hmanh.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private int mrqPrice;
    private int sellingPrice;
    private int discountPercent;
    private int quantity;
    private String brand;
    private String color;
    private List<String> images;
    private int numRatings;
    private CategoryResponse category;
    private SellerResponse seller;
    private List<String> sizes;
    private boolean active;
    private boolean inStock;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}