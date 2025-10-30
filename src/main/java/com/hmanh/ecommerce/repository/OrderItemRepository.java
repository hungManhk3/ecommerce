package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {
}
