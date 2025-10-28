package com.hmanh.ecommerce.Entity;

import com.hmanh.ecommerce.domain.USER_ROLE;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String email;
    private String fullName;
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
    private String mobile;
    @OneToMany
    private Set<Address> addresses = new HashSet<>();
    @ManyToMany
    @JoinTable
    private Set<Coupon> usedCoupons = new HashSet<>();

}
