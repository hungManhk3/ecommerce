package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.*;
import com.hmanh.ecommerce.domain.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user, Address address, Cart cart);
    Order findOrderById(Long id);
    List<Order> userOrderHistory( Long userId);
    List<Order> sellerOrders(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);
    Order cancelOrder(Long orderId, User user);
}
