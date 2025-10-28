package com.hmanh.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User customer;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Seller seller;
    private LocalDateTime date =  LocalDateTime.now();
}
