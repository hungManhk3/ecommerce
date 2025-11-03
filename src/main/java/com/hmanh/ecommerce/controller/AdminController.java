package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.domain.AccountStatus;
import com.hmanh.ecommerce.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final SellerService sellerService;

    @PatchMapping("/seller/{sellerId}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(@PathVariable Long sellerId, @PathVariable AccountStatus status) throws Exception {
        Seller update = sellerService.updateSellerAccountStatus(sellerId, status);
        return ResponseEntity.ok(update);
    }
}
