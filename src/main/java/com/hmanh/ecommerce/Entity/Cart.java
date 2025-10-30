package com.hmanh.ecommerce.Entity;

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
public class Cart extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User user;                  // nguoi dung so huu gio hang
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>(); // danh sach item trong gio
    private int totalSellingPrice;                       // tong gia ban
    private int totalQuantity;
    private int totalMrpPrice;                  // tong gia niem yet
    private int discount;                       // tong tien giam gia
    private String couponCode;                  //ma giam gia ap dung
}
