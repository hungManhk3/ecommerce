package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.*;
import com.hmanh.ecommerce.domain.OrderStatus;
import com.hmanh.ecommerce.dto.request.CreateOrderRequest;
import com.hmanh.ecommerce.dto.request.UpdateStatusRequest;
import com.hmanh.ecommerce.dto.response.CartResponse;
import com.hmanh.ecommerce.dto.response.OrderItemResponse;
import com.hmanh.ecommerce.dto.response.OrderResponse;
import com.hmanh.ecommerce.dto.response.PaymentLinkResponse;
import com.hmanh.ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody CreateOrderRequest request, @RequestHeader("Authorization") String jwt
            ) {
        try {
            User user = userService.findUserByJwtToken(jwt);
            Cart cart = cartService.findUserCart1(user);
            Set<OrderResponse> orders = orderService.createOrder(user, request.getAddress(), cart);
            return ResponseEntity.status(HttpStatus.CREATED).body(orders);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponse>> getUser(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<OrderResponse> responses = orderService.userOrderHistory(user.getId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@RequestHeader("Authorization") String jwt, @PathVariable("orderId") Long orderId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        OrderResponse order = orderService.findOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItem(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long orderId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        OrderItemResponse orderItemResponse = orderService.findById(orderId);
        return ResponseEntity.ok(orderItemResponse);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@RequestHeader("Authorization") String jwt, @PathVariable("orderId") Long orderId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        OrderResponse orderResponse = orderService.cancelOrder(orderId, user);
        Seller seller = sellerService.getSellerById(orderResponse.getSellerId());
        SellerReport sellerReport = sellerReportService.getSellerReport(seller);
        sellerReport.setCanceledOrders(sellerReport.getCanceledOrders() + 1);
        sellerReport.setTotalRefunds(sellerReport.getTotalRefunds() + orderResponse.getTotalSellingPrice());
        sellerReportService.updateSellerReport(sellerReport);
        return ResponseEntity.ok(orderResponse);
    }

}
