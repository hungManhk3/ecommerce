package com.hmanh.ecommerce.dto.response;

import com.hmanh.ecommerce.domain.OrderStatus;
import com.hmanh.ecommerce.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderId;
    private UserResponse user;
    private Long sellerId;
//    private SellerResponse seller; // Thông tin seller
    private List<OrderItemResponse> orderItems;
    private AddressResponse address;
    private PaymentDetailsResponse paymentDetails;
    private double totalMrpPrice;
    private Integer totalSellingPrice;
    private Integer discount;
    private OrderStatus orderStatus;
    private int totalItem;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
