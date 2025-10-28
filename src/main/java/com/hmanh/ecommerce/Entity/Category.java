package com.hmanh.ecommerce.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotNull
    @Column(unique = true)
    private String categoryId;
    @ManyToOne
    private Category parent;
    @NotNull
    private Integer level;
}
