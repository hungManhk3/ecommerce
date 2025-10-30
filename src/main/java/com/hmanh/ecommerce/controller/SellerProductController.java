package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.dto.request.CreateProductRequest;
import com.hmanh.ecommerce.dto.request.UpdateProductRequest;
import com.hmanh.ecommerce.dto.response.ProductResponse;
import com.hmanh.ecommerce.exception.ProductException;
import com.hmanh.ecommerce.exception.SellerException;
import com.hmanh.ecommerce.service.ProductService;
import com.hmanh.ecommerce.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProductController {
    private final ProductService productService;
    private final SellerService sellerService;

    @PostMapping()
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request, @RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        try {
            ProductResponse productResponse = productService.createProduct(request, seller);
            return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) throws Exception {
        try
            {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.OK);
            }
        catch (ProductException pe) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable Long id, @RequestBody UpdateProductRequest request, @RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        try {
            ProductResponse productResponse = productService.updateProduct(id, request, seller);
            return ResponseEntity.ok(productResponse);
        }
        catch (ProductException pe) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ProductResponse>> getProductsBySellerId(@PathVariable Long id) throws SellerException {
        List<ProductResponse> products = productService.getProductsBySellerId(id);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
