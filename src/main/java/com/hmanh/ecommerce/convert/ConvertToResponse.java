package com.hmanh.ecommerce.convert;

import com.hmanh.ecommerce.Entity.*;
import com.hmanh.ecommerce.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    public CartResponse convertToCartResponse(Cart cart) {
        if (cart == null) return null;

        return CartResponse.builder()
                .id(cart.getId())
                .user(convertToUserResponse(cart.getUser()))
                .cartItems(convertToCartItemResponseSet(cart.getCartItems()))
                .totalSellingPrice(cart.getTotalSellingPrice())
                .totalQuantity(cart.getTotalQuantity())
                .totalMrpPrice(cart.getTotalMrpPrice())
                .discount(cart.getDiscount())
                .couponCode(cart.getCouponCode())
                .createdDate(cart.getCreatedDate())
                .modifiedDate(cart.getModifiedDate())
                .build();
    }

    public CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        if (cartItem == null) return null;

        int totalMrpPrice = (cartItem.getMrpPrice() != null ? cartItem.getMrpPrice() : 0) * cartItem.getQuantity();
        int totalSellingPrice = (cartItem.getSellingPrice() != null ? cartItem.getSellingPrice() : 0) * cartItem.getQuantity();
        int discount = Math.max(0, totalMrpPrice - totalSellingPrice);

        return CartItemResponse.builder()
                .id(cartItem.getId())
                .product(convertToProductResponse(cartItem.getProduct()))
                .size(cartItem.getSize())
                .quantity(cartItem.getQuantity())
                .mrpPrice(cartItem.getMrpPrice())
                .sellingPrice(cartItem.getSellingPrice())
                .totalMrpPrice(totalMrpPrice)
                .totalSellingPrice(totalSellingPrice)
                .discount(discount)
                .createdDate(cartItem.getCreatedDate())
                .modifiedDate(cartItem.getModifiedDate())
                .build();
    }

    public Set<CartItemResponse> convertToCartItemResponseSet(Set<CartItem> cartItems) {
        if (cartItems == null) return Set.of();

        return cartItems.stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toSet());
    }
    public UserResponse convertToUserResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .mobile(user.getMobile())
                .addresses(convertToAddressResponseSet(user.getAddresses()))
                .coupons(convertToCouponResponseSet(user.getUsedCoupons()))
                .build();
    }

    public CouponResponse convertToCouponResponse(Coupon coupon) {
        if (coupon == null) return null;

        LocalDate today = LocalDate.now();
        boolean expired = today.isAfter(coupon.getEndDate());
        boolean usable = coupon.isActive() &&
                !expired &&
                today.isAfter(coupon.getStartDate().minusDays(1));

        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountPercent(coupon.getDiscountPercent())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .minimumOrderValue(coupon.getMinimumOrderValue())
                .active(coupon.isActive())
                .expired(expired)
                .usable(usable)
                .build();
    }
    public Set<AddressResponse> convertToAddressResponseSet(Set<Address> addresses) {
        if (addresses == null) return Set.of();

        return addresses.stream()
                .map(this::convertToAddressResponse)
                .collect(Collectors.toSet());
    }

    public Set<CouponResponse> convertToCouponResponseSet(Set<Coupon> coupons) {
        if (coupons == null) return Set.of();

        return coupons.stream()
                .map(this::convertToCouponResponse)
                .collect(Collectors.toSet());
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
