package com.hmanh.ecommerce.dto.request;

import com.hmanh.ecommerce.Entity.Address;
import com.hmanh.ecommerce.Entity.Cart;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private Address address;
    private String paymentMethod;
}
