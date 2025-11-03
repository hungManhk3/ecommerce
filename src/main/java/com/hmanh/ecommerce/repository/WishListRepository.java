package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    WishList findByUserId(Long userId);
}
