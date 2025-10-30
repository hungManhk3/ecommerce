package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.*;
import com.hmanh.ecommerce.domain.OrderStatus;
import com.hmanh.ecommerce.dto.response.OrderResponse;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<OrderResponse> createOrder(User user, Address address, Cart cart);
    OrderResponse findOrderById(Long id);
    List<OrderResponse> userOrderHistory( Long userId);
    List<OrderResponse> sellerOrders(Long sellerId);
    OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus);
    OrderResponse cancelOrder(Long orderId, User user);
}
