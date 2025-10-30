package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.CartItem;
import com.hmanh.ecommerce.dto.response.CartItemResponse;

public interface CartItemService {
    CartItemResponse updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception;
    void removeCartItem(Long userId, Long id) throws Exception;
    CartItem findCartItemById(Long id);
}
