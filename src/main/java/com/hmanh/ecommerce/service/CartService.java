package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.CartItem;
import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.dto.request.AddItemRequest;
import com.hmanh.ecommerce.dto.response.CartItemResponse;
import com.hmanh.ecommerce.dto.response.CartResponse;
import com.hmanh.ecommerce.exception.ProductException;

public interface CartService {
    CartItemResponse addCartItem(User user, AddItemRequest request) throws ProductException;
    CartResponse findUserCart(User user);
    Cart findProductCart(Product product);
}
