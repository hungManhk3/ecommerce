package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Category;
import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.dto.request.CreateProductRequest;
import com.hmanh.ecommerce.exception.ProductException;
import com.hmanh.ecommerce.repository.CategoryRepository;
import com.hmanh.ecommerce.repository.ProductRepository;
import com.hmanh.ecommerce.service.FileUploadService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;

    @Override
    public Product createProduct(CreateProductRequest request, Seller seller) {
//        String imgUml = fileUploadService.upload(file);
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
        product.setBrand(request.getBrand());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product updatedProduct) throws ProductException {
        try {
            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductException("Product not found with id: " + productId));

            if (updatedProduct.getTitle() != null) {
                existingProduct.setTitle(updatedProduct.getTitle());
            }

            if (updatedProduct.getDescription() != null) {
                existingProduct.setDescription(updatedProduct.getDescription());
            }

            if (updatedProduct.getColor() != null) {
                existingProduct.setColor(updatedProduct.getColor());
            }

            if (updatedProduct.getSize() != null) {
                existingProduct.setSize(updatedProduct.getSize());
            }

            if (updatedProduct.getBrand() != null) {
                existingProduct.setBrand(updatedProduct.getBrand());
            }

            // Update prices and recalculate discount if either price changes
            if (updatedProduct.getMrqPrice() > 0) {
                existingProduct.setMrqPrice(updatedProduct.getMrqPrice());
            }

            if (updatedProduct.getSellingPrice() > 0) {
                existingProduct.setSellingPrice(updatedProduct.getSellingPrice());
            }

            // Recalculate discount percentage if prices were updated
            if (updatedProduct.getMrqPrice() > 0 || updatedProduct.getSellingPrice() > 0) {
                int discountPercentage = calculateDiscountPercentage(
                        existingProduct.getMrqPrice(),
                        existingProduct.getSellingPrice()
                );
                existingProduct.setDiscountPercent(discountPercentage);
            }

            if (updatedProduct.getQuantity() >= 0) {
                existingProduct.setQuantity(updatedProduct.getQuantity());
            }

            if (updatedProduct.getImages() != null && !updatedProduct.getImages().isEmpty()) {
                existingProduct.setImages(updatedProduct.getImages());
            }
            if (updatedProduct.getCategory() != null) {
                existingProduct.setCategory(updatedProduct.getCategory());
            }

            return productRepository.save(existingProduct);

        } catch (Exception e) {
            throw new ProductException("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public Product getProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(() -> new ProductException("product not found"));
    }

    @Override
    public List<Product> getAllProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> searchProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Pageable pageable) {
        Specification<Product> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (brand != null && !brand.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("brand"), brand));
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
        if (sort != null && !sort.isEmpty()) {
            int page = pageNumber != null ? pageNumber : 0;
            int size = pageable.getPageSize();

            switch (sort) {
                case "price_low":
                    pageable = PageRequest.of(page, size, Sort.by("sellingPrice").ascending());
                    break;
                case "price_high": // Fixed typo
                    pageable = PageRequest.of(page, size, Sort.by("sellingPrice").descending());
                    break;
                case "newest":
                    pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                    break;
                case "popular":
                    pageable = PageRequest.of(page, size, Sort.by("numRatings").descending());
                    break;
                default:
                    pageable = PageRequest.of(page, size, Sort.by("id").descending());
            }
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("id").descending());
        }
        return productRepository.findAll(specification, pageable);
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
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
