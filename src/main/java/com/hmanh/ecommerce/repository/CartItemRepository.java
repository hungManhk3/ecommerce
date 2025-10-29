package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.CartItem;
import com.hmanh.ecommerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
