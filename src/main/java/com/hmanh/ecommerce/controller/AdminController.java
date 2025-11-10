package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.domain.AccountStatus;
import com.hmanh.ecommerce.service.FileUploadService;
import com.hmanh.ecommerce.service.SellerService;
import com.stripe.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final SellerService sellerService;
    private final FileUploadService fileService;

    @PatchMapping("/seller/{sellerId}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(@PathVariable Long sellerId, @PathVariable AccountStatus status) throws Exception {
        Seller update = sellerService.updateSellerAccountStatus(sellerId, status);
        return ResponseEntity.ok(update);
    }
}
