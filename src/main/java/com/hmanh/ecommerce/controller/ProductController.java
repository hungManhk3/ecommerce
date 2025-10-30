package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.dto.response.ApiResponse;
import com.hmanh.ecommerce.dto.response.ProductResponse;
import com.hmanh.ecommerce.exception.ProductException;
import com.hmanh.ecommerce.repository.ProductRepository;
import com.hmanh.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("productId") Long productId) throws ProductException {
        ProductResponse productResponse = productService.getProductById(productId);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) String query) {
        List<Product> products = productService.getAllProducts(query);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            Pageable pageable){
        Page<ProductResponse> page = productService.searchProducts(category, brand, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageable);

        return ApiResponse.<List<ProductResponse>>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}
