package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.dto.request.CreateProductRequest;
import com.hmanh.ecommerce.exception.ProductException;
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
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request, @RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(request, seller);
        return ResponseEntity.ok().body(product);
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
    public ResponseEntity<Product> updateProductById(@PathVariable Long id, @RequestBody Product product) throws Exception {
        try {
            productService.updateProduct(id, product);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        catch (ProductException pe) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Product>> getProductsBySellerId(@PathVariable Long id) {
        List<Product> products = productService.getProductsBySellerId(id);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
