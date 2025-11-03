package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory,Long> {

}
