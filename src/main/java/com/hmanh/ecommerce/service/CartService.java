package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.CartItem;
import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.User;

public interface CartService {
    CartItem addCartItem(User user, Product product, String size, int quantity);
    Cart findUserCart(User user);
    Cart findProductCart(Product product);
}
