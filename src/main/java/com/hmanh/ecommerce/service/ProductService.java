package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.dto.request.CreateProductRequest;
import com.hmanh.ecommerce.dto.request.UpdateProductRequest;
import com.hmanh.ecommerce.dto.response.ProductResponse;
import com.hmanh.ecommerce.exception.ProductException;
import com.hmanh.ecommerce.exception.SellerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest createProductRequest, Seller seller);
    void deleteProduct(Long productId) throws ProductException;
    ProductResponse updateProduct(Long productId, UpdateProductRequest request, Seller seller) throws ProductException;
    ProductResponse getProductById(Long productId) throws ProductException;
    Product getproductById(Long productId) throws ProductException;
    List<Product> getAllProducts(String query);
    Page<ProductResponse> searchProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Pageable pageable);
    List<ProductResponse> getProductsBySellerId(Long sellerId) throws SellerException;
}
