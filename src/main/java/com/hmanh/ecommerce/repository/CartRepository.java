package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
