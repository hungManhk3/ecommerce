package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.dto.request.CreateProductRequest;
import com.hmanh.ecommerce.exception.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest createProductRequest, Seller seller);
    void deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long  productId, Product product) throws ProductException;
    Product getProductById(Long productId) throws ProductException;
    List<Product> getAllProducts(String query);
    Page<Product> searchProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber,
            Pageable pageable);
    List<Product> getProductsBySellerId(Long sellerId);
}
