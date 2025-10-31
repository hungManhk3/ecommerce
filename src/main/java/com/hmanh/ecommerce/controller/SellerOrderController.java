package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.dto.request.UpdateStatusRequest;
import com.hmanh.ecommerce.dto.response.OrderResponse;
import com.hmanh.ecommerce.service.OrderService;
import com.hmanh.ecommerce.service.SellerReportService;
import com.hmanh.ecommerce.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/seller/orders")
public class SellerOrderController {
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> getSellerOrders(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<OrderResponse> responses = orderService.sellerOrders(seller.getId());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@RequestHeader("Authorization") String jwt, @RequestBody UpdateStatusRequest request, @PathVariable("orderId") Long orderId) throws Exception {
        OrderResponse response = orderService.updateOrderStatus(orderId, request.getOrderStatus());
        return ResponseEntity.ok(response);
    }
}
