package com.hmanh.ecommerce.dto.request;

import com.hmanh.ecommerce.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusRequest {
    private OrderStatus orderStatus;
}
