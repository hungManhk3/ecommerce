package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.domain.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws Exception;
    Seller getSellerByEmail(String email) throws Exception;
    Page<Seller> getAllSellers(AccountStatus status, Pageable pageable);
    Seller updateSeller(Long id, Seller seller);
    void deleteSeller(Long id) throws Exception;
    Seller verifyEmail(String email, String otp) throws Exception;
    Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception;
}
