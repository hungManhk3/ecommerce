package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Category;
import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.dto.request.CreateProductRequest;
import com.hmanh.ecommerce.exception.ProductException;
import com.hmanh.ecommerce.repository.CategoryRepository;
import com.hmanh.ecommerce.repository.ProductRepository;
import com.hmanh.ecommerce.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(CreateProductRequest request, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(request.getCategory());
        if  (category1 == null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory());
            category.setLevel(1);
            category1 =  categoryRepository.save(category);
        }
        Category category2 = categoryRepository.findByCategoryId(request.getCategory2());
        if  (category2 == null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory2());
            category.setLevel(2);
            category.setParent(category1);
            category2 =  categoryRepository.save(category);
        }
        Category category3 = categoryRepository.findByCategoryId(request.getCategory3());
        if  (category3 == null) {
            Category category = new Category();
            category.setCategoryId(request.getCategory3());
            category.setLevel(3);
            category.setParent(category2);
            category3 =  categoryRepository.save(category);
        }
        int discountPercentage = calculateDiscountPercentage(request.getMrpPrice(), request.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(request.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setSellingPrice(request.getSellingPrice());
        product.setImages(request.getImages());
        product.setMrqPrice(request.getMrpPrice());
        product.setSize(request.getSizes());
        product.setDiscountPercent(discountPercentage);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public Product getProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(() -> new ProductException("product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Page<Product> searchProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Pageable pageable) {
        Specification<Product> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (brand != null) {

            }
            if (colors != null) {
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
            if (sizes != null) {
                predicates.add(criteriaBuilder.equal(root.get("size"), sizes));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        Pageable pageable1;
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price_low":
                    pageable1 = PageRequest.of(pageNumber!=null? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                    break;
            }
        }
        return null;
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return List.of();
    }
    Integer calculateDiscountPercentage(double mrpPrice, double sellingPrice) {
        if (mrpPrice <= 0 || sellingPrice <= 0) {
            throw new IllegalArgumentException("Than 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int)discountPercentage;
    }
}
