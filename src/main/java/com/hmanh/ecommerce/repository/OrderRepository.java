package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long id);
    List<Order> findBySellerId(Long id);
}
