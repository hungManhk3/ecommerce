package com.hmanh.ecommerce.convert;

import com.hmanh.ecommerce.Entity.*;
import com.hmanh.ecommerce.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvertToResponse {
    public ProductResponse convertToProductResponse(Product product) {
        if (product == null) return null;
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getMrqPrice(),
                product.getSellingPrice(),
                product.getDiscountPercent(),
                product.getQuantity(),
                product.getBrand(),
                product.getColor(),
                product.getImages(),
                product.getNumRatings(),
                convertToCategoryResponse(product.getCategory()),
                convertToSellerResponse(product.getSeller()),
                product.getSizes(),
                product.isActive(),
                product.isInStock(),
                product.getCreatedDate(),
                product.getModifiedDate()
        );
    }
    public CategoryResponse convertToCategoryResponse(Category category) {
        if (category == null) return null;

        CategoryResponse parentResponse = null;
        if (category.getParent() != null) {
            parentResponse = convertToCategoryResponse(category.getParent());
        }

        return new CategoryResponse(
                category.getId(),
                category.getCategoryId(),
                category.getName(),
                category.getLevel(),
                parentResponse
        );
    }

    public SellerResponse convertToSellerResponse(Seller seller) {
        if (seller == null) return null;

        return new SellerResponse(
                seller.getId(),
                seller.getSellerName(),
                seller.getMobile(),
                seller.getEmail(),
                convertToBusinessDetailsResponse(seller.getBusinessDetails()),
                convertToBankDetailsResponse(seller.getBankDetails()),
                convertToAddressResponse(seller.getPickupAddress()),
                seller.getGSTIN(),
                seller.getRole(),
                seller.isEmailVerified(),
                seller.isDeleted(),
                seller.getStatus()
        );
    }
    public BusinessDetailsResponse convertToBusinessDetailsResponse(BusinessDetails businessDetails) {
        if (businessDetails == null) return null;

        return new BusinessDetailsResponse(
                businessDetails.getBusinessName(),
                businessDetails.getBusinessEmail(),
                businessDetails.getBusinessMobile(),
                businessDetails.getBusinessAddress(),
                businessDetails.getLogo(),
                businessDetails.getBanner()
        );
    }
    public BankDetailsResponse convertToBankDetailsResponse(BankDetails bankDetails) {
        if (bankDetails == null) return null;

        return new BankDetailsResponse(
                bankDetails.getAccountNumber(),
                bankDetails.getAccountHolderName(),
                bankDetails.getIfscCode()
        );
    }
    public AddressResponse convertToAddressResponse(Address address) {
        if (address == null) return null;

        return new AddressResponse(
                address.getId(),
                address.getName(),
                address.getLocality(),
                address.getAddress(),
                address.getCity(),
                address.getState(),
                address.getCountry()
        );
    }

    public List<ProductResponse> convertToProductResponseList(List<Product> products) {
        if (products == null) return new ArrayList<>();

        return products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    public Page<ProductResponse> convertToProductResponsePage(Page<Product> productPage) {
        return productPage.map(this::convertToProductResponse);
    }


}
