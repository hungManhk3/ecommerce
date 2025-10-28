package com.hmanh.ecommerce.Entity;

import com.hmanh.ecommerce.domain.PaymentMethod;
import com.hmanh.ecommerce.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;
    private PaymentMethod  paymentMethod;
    private String paymentLinkId;
    @ManyToOne
    private User user;
    @OneToMany
    private Set<Order>  orders = new HashSet<>();
}
