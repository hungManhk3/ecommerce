package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Category;
import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.convert.ConvertToResponse;
import com.hmanh.ecommerce.dto.request.CreateProductRequest;
import com.hmanh.ecommerce.dto.request.UpdateProductRequest;
import com.hmanh.ecommerce.dto.response.ProductResponse;
import com.hmanh.ecommerce.exception.ProductException;
import com.hmanh.ecommerce.exception.SellerException;
import com.hmanh.ecommerce.repository.CategoryRepository;
import com.hmanh.ecommerce.repository.ProductRepository;
import com.hmanh.ecommerce.repository.SellerRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ConvertToResponse convertToResponse;
    private final SellerRepository sellerRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request, Seller seller) {
        // check request
        validateCreateProductRequest(request);
        Category category3  = handleCategory(request.getCategory(), request.getCategory2(), request.getCategory3());

        int discountPercentage = calculateDiscountPercentage(request.getMrpPrice(), request.getSellingPrice());

        Product product = Product.builder()
                .seller(seller)
                .category(category3)
                .title(request.getTitle())
                .description(request.getDescription())
                .color(request.getColor())
                .brand(request.getBrand())
                .sizes(request.getSizes() != null ? Collections.singletonList(request.getSizes()) : new ArrayList<>())
                .mrqPrice(request.getMrpPrice())
                .sellingPrice(request.getSellingPrice())
                .discountPercent(discountPercentage)
                .quantity(request.getQuantity())
                .images(request.getImages())
                .numRatings(0)
                .active(true)
                .inStock(request.getQuantity() > 0)
                .build();
        Product savedProduct = productRepository.save(product);
        return convertToResponse.convertToProductResponse(savedProduct);
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductException("product not found"));
        productRepository.delete(product);
    }

    @Override
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request, Seller seller) throws ProductException {
        Product exitingProduct = productRepository.findById(productId).orElseThrow(() -> new ProductException("Product not found"));
        if (!exitingProduct.getSeller().getId().equals(seller.getId())) {
            throw new ProductException("You are not allowed to update this product");
        }
        validateUpdateProductRequest(request);

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            exitingProduct.setTitle(request.getTitle().trim());
        }
        if (request.getDescription() != null){
            exitingProduct.setDescription(request.getDescription());
        }
        if (request.getMrpPrice() != null){
            exitingProduct.setMrqPrice(request.getMrpPrice());
        }
        if (request.getSellingPrice() != null){
            exitingProduct.setSellingPrice(request.getSellingPrice());
        }
        if (request.getColor() != null){
            exitingProduct.setColor(request.getColor());
        }
        if (request.getImages() != null) {
            exitingProduct.setImages(request.getImages());
        }
        if (request.getSizes() != null) {
            exitingProduct.setSizes(request.getSizes());
        }
        if (request.getBrand() != null) {
            exitingProduct.setBrand(request.getBrand());
        }
        if (request.getQuantity() != null) {
            exitingProduct.setQuantity(request.getQuantity());
        }
        if (request.getActive() != null) {
            exitingProduct.setActive(request.getActive());
        }
        if (request.getMrpPrice() != null || request.getSellingPrice() != null) {
            int mrpPrice = request.getMrpPrice() != null ? request.getMrpPrice() : exitingProduct.getMrqPrice();
            int sellingPrice = request.getSellingPrice() != null ? request.getSellingPrice() : exitingProduct.getSellingPrice();
            int discountPercentage = calculateDiscountPercentage(mrpPrice, sellingPrice);
            exitingProduct.setDiscountPercent(discountPercentage);
        }
        if (request.getCategory() != null && request.getCategory2() != null && request.getCategory3() != null) {
            Category newCategory = handleCategory(
                    request.getCategory(),
                    request.getCategory2(),
                    request.getCategory3()
            );
            exitingProduct.setCategory(newCategory);
        }
        if (request.getQuantity() != null) {
            exitingProduct.setInStock(request.getQuantity() > 0);
        }
        Product updateProduct = productRepository.save(exitingProduct);
        return convertToResponse.convertToProductResponse(updateProduct);
    }

    @Override
    public ProductResponse getProductById(Long productId) throws ProductException {
         Product product = productRepository.findById(productId).orElseThrow(() -> new ProductException("product not found"));
         return convertToResponse.convertToProductResponse(product);
    }
    @Override
    public Product getproductById(Long productId) throws ProductException {
         return productRepository.findById(productId).orElseThrow(() -> new ProductException("product not found"));
    }

    @Override
    public List<Product> getAllProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<ProductResponse> searchProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Pageable pageable) {
        Specification<Product> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isTrue(root.get("active")));
            if (category != null && !category.trim().isEmpty()) {
                Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category.trim()));
            }
            if (brand != null && !brand.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.trim().toLowerCase()));
            }
            if (colors != null && !colors.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
//            if (colors != null && !colors.trim().isEmpty()) {
//                String[] colorArray = colors.split(",");
//                List<Predicate> colorPredicates = new ArrayList<>();
//                for (String color : colorArray) {
//                    colorPredicates.add(criteriaBuilder.equal(
//                            criteriaBuilder.lower(root.get("color")),
//                            color.trim().toLowerCase()
//                    ));
//                }
//                predicates.add(criteriaBuilder.or(colorPredicates.toArray(new Predicate[0])));
//            }
            if (sizes != null && !sizes.trim().isEmpty()) {
                String[] sizeArray = sizes.split(",");
                List<Predicate> sizePredicates = new ArrayList<>();
                for (String size : sizeArray) {
                    sizePredicates.add(criteriaBuilder.isMember(size.trim(), root.get("size")));
                }
                predicates.add(criteriaBuilder.or(sizePredicates.toArray(new Predicate[0])));
            }
            if (minPrice != null && minPrice > 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null && maxPrice > 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null && minDiscount > 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }
            if (stock != null && !stock.trim().isEmpty()) {
                if ("in_stock".equalsIgnoreCase(stock.trim())) {
                    predicates.add(criteriaBuilder.greaterThan(root.get("quantity"), 0));
                    predicates.add(criteriaBuilder.isTrue(root.get("inStock")));
                } else if ("out_of_stock".equalsIgnoreCase(stock.trim())) {
                    predicates.add(criteriaBuilder.equal(root.get("quantity"), 0));
                    predicates.add(criteriaBuilder.isFalse(root.get("inStock")));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        Pageable finalPageable = buildPageable(pageable, sort);

        Page<Product> productPage = productRepository.findAll(specification, finalPageable);

        return convertToResponse.convertToProductResponsePage(productPage);
    }
    @Override
    public List<ProductResponse> getProductsBySellerId(Long sellerId) throws SellerException {
        if (!sellerRepository.existsById(sellerId)) {
            throw new SellerException("Seller not found");
        }
        List<Product> products = productRepository.findBySellerId(sellerId);
        return convertToResponse.convertToProductResponseList(products);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0 || sellingPrice >= mrpPrice) {
            return 0;
        }
        return (int) Math.round(((mrpPrice - sellingPrice) / (double) mrpPrice) * 100);
    }

    private void validateCreateProductRequest(CreateProductRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Product title is required");
        }
        if (request.getMrpPrice() <= 0) {
            throw new IllegalArgumentException("MRP price must be greater than 0");
        }
        if (request.getSellingPrice() <= 0) {
            throw new IllegalArgumentException("Selling price must be greater than 0");
        }
        if (request.getSellingPrice() > request.getMrpPrice()) {
            throw new IllegalArgumentException("Selling price cannot be greater than MRP price");
        }
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }
    }

    private void validateUpdateProductRequest(UpdateProductRequest request) {
        if (request.getMrpPrice() != null && request.getMrpPrice() <= 0) {
            throw new IllegalArgumentException("MRP price must be greater than 0");
        }
        if (request.getSellingPrice() != null && request.getSellingPrice() <= 0) {
            throw new IllegalArgumentException("Selling price must be greater than 0");
        }
        if (request.getMrpPrice() != null && request.getSellingPrice() != null
                && request.getSellingPrice() > request.getMrpPrice()) {
            throw new IllegalArgumentException("Selling price cannot be greater than MRP price");
        }
        if (request.getQuantity() != null && request.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    private Category handleCategory(String category1Id, String category2Id, String category3Id) {
        // level 1
        Category category1 = categoryRepository.findByCategoryId(category1Id);
        if (category1 == null) {
            category1 = Category.builder()
                    .categoryId(category1Id)
                    .name(category1Id)
                    .level(1)
                    .build();
            category1 = categoryRepository.save(category1);
        }
        // level 2
        Category category2 = categoryRepository.findByCategoryId(category2Id);
        if (category2 == null) {
            category2 = Category.builder()
                    .categoryId(category2Id)
                    .name(category2Id)
                    .level(2)
                    .parent(category1)
                    .build();
            category2 = categoryRepository.save(category2);
        }
        // level 3
        Category category3 = categoryRepository.findByCategoryId(category3Id);
        if (category3 == null) {
            category3 = Category.builder()
                    .categoryId(category3Id)
                    .name(category3Id)
                    .level(3)
                    .parent(category2)
                    .build();
            category3 = categoryRepository.save(category3);
        }
        return category3;
    }

    private Pageable buildPageable(Pageable pageable, String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return pageable;
        }

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        switch (sort.toLowerCase()) {
            case "price_low":
                return PageRequest.of(page, size, Sort.by("sellingPrice").ascending());
            case "price_high":
                return PageRequest.of(page, size, Sort.by("sellingPrice").descending());
            case "newest":
                return PageRequest.of(page, size, Sort.by("createdDate").descending());
            case "oldest":
                return PageRequest.of(page, size, Sort.by("createdDate").ascending());
            case "popular":
                return PageRequest.of(page, size, Sort.by("numRatings").descending());
            case "discount_high":
                return PageRequest.of(page, size, Sort.by("discountPercent").descending());
            case "name_asc":
                return PageRequest.of(page, size, Sort.by("title").ascending());
            case "name_desc":
                return PageRequest.of(page, size, Sort.by("title").descending());
            default:
                return PageRequest.of(page, size, Sort.by("createDate").descending());
        }
    }
}
