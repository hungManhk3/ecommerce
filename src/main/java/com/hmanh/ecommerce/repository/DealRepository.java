package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
