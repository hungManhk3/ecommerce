package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long id);
    List<Order> findBySellerId(Long id);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.user.id = :userId ORDER BY o.orderDate DESC")
    List<Order> findByUserIdWithItems(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.sellerId = :sellerId ORDER BY o.orderDate DESC")
    List<Order> findBySellerIdWithItems(@Param("sellerId") Long sellerId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.orderId = :orderId")
    Optional<Order> findByOrderIdWithItems(@Param("orderId") String orderId);
}
