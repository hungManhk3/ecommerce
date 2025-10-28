package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.domain.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    @Query("""
        SELECT s FROM Seller s
        WHERE s.isDeleted = false
        AND s.id = :id
""")
    Seller findSellerById(long id);

    @Query("""
        SELECT s FROM Seller s
        WHERE s.isDeleted = false
        AND s.email = :email
""")
    Seller findByEmail(String email);

    @Query("""
        SELECT s FROM Seller s
        WHERE s.isDeleted = false
        AND (:status IS NULL OR s.status = :status)
        ORDER BY s.id
""")
    Page<Seller> searchSellers(@Param("status") AccountStatus status, Pageable pageable);
}
