package com.hmanh.ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private Cart cart; // gio hang chua item nay
    @ManyToOne
    private Product product;
    private String size;
    private int quantity = 1;
    private Integer mrpPrice;
    private Integer sellingPrice;
}
