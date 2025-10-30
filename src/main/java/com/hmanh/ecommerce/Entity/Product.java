package com.hmanh.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private int mrqPrice;      // gia niem yet
    private int sellingPrice;   // gia thuc te
    private int discountPercent; //% giam gia
    private int quantity;           // so luong ton kho
    private String brand;
    private String color;
    @ElementCollection
    private List<String> images = new ArrayList<>();
    private int numRatings;
    @ManyToOne
    private Category category;      // danh muc
    @ManyToOne
    private Seller seller;              // nguoi ban
    private List<String> sizes;              // kich thuoc
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    private boolean active = true;  // Ẩn/hiện sản phẩm
    private boolean inStock = true; // Còn hàng/hết hàng
}
