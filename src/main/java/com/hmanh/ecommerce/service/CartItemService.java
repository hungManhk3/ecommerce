package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception;
    void removeCartItem(Long userId, Long id) throws Exception;
    CartItem findCartItemById(Long id);
}
