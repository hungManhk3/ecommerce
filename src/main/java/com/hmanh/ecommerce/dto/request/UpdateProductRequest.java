package com.hmanh.ecommerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    private String title;
    private String description;
    private Integer mrpPrice;
    private Integer sellingPrice;
    private String color;
    private List<String> images;
    private String category;
    private String category2;
    private String category3;
    private List<String> sizes;
    private String brand;
    private Integer quantity;
    private Boolean active;
}
