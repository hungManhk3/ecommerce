package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.CartItem;
import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.convert.ConvertToResponse;
import com.hmanh.ecommerce.dto.request.AddItemRequest;
import com.hmanh.ecommerce.dto.response.CartItemResponse;
import com.hmanh.ecommerce.dto.response.CartResponse;
import com.hmanh.ecommerce.exception.ProductException;
import com.hmanh.ecommerce.repository.CartItemRepository;
import com.hmanh.ecommerce.repository.CartRepository;
import com.hmanh.ecommerce.repository.ProductRepository;
import com.hmanh.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository  productRepository;
    private final ConvertToResponse convertToResponse;

    @Override
    public CartItemResponse addCartItem(User user, AddItemRequest request) throws ProductException {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> createNewCart(user));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ProductException("product not found"));
        validateAddCartItemRequest(product, request.getSize(), request.getQuantity());
        CartItem isExits = cartItemRepository.findByCartAndProductAndSize(cart, product, request.getSize());
        CartItem cartItem;
        if (isExits != null){
            isExits.setQuantity(isExits.getQuantity() + request.getQuantity());
            cartItem = cartItemRepository.save(isExits);
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .size(request.getSize())
                    .quantity(request.getQuantity())
                    .mrpPrice(product.getMrqPrice())
                    .sellingPrice(product.getSellingPrice())
                    .build();

            CartItem savedCartItem = cartItemRepository.save(cartItem);
            cart.getCartItems().add(savedCartItem);
            cartItem = savedCartItem;
        }
        updateCartTotals(cart);
        return convertToResponse.convertToCartItemResponse(cartItem);
    }

    @Override
    public CartResponse findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> createNewCart(user));
        updateCartTotals(cart);
        return convertToResponse.convertToCartResponse(cart);
    }

    @Override
    public Cart findUserCart1(User user) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> createNewCart(user));
        updateCartTotals(cart);
        return cart;
    }

    @Override
    public Cart findProductCart(Product product) {
        return null;
    }
    private Cart createNewCart(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .cartItems(new HashSet<>())
                .totalSellingPrice(0)
                .totalQuantity(0)
                .totalMrpPrice(0)
                .discount(0)
                .build();
        return cartRepository.save(cart);
    }

    private void updateCartTotals(Cart cart) {
        int totalQuantity = 0;
        int totalMrpPrice = 0;
        int totalSellingPrice = 0;

        for (CartItem item : cart.getCartItems()) {
            totalQuantity += item.getQuantity();
            totalMrpPrice += (item.getMrpPrice() != null ? item.getMrpPrice() : 0) * item.getQuantity();
            totalSellingPrice += (item.getSellingPrice() != null ? item.getSellingPrice() : 0) * item.getQuantity();
        }

        int discount = Math.max(0, totalMrpPrice - totalSellingPrice);

        cart.setTotalQuantity(totalQuantity);
        cart.setTotalMrpPrice(totalMrpPrice);
        cart.setTotalSellingPrice(totalSellingPrice);
        cart.setDiscount(discount);

        cartRepository.save(cart);
    }

    private void validateAddCartItemRequest(Product product, String size, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }
        if (!product.isActive()) {
            throw new IllegalArgumentException("Product is not available");
        }
        if (!product.isInStock()) {
            throw new IllegalArgumentException("Product is out of stock");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Only " + product.getQuantity() + " items available in stock"
            );
        }
        if (size != null && !product.getSizes().contains(size)) {
            throw new IllegalArgumentException(
                    "Size " + size + " is not available for this product. Available sizes: " + product.getSizes()
            );
        }
    }
}
