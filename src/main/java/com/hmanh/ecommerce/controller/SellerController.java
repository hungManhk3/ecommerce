package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.Entity.VerificationCode;
import com.hmanh.ecommerce.domain.AccountStatus;
import com.hmanh.ecommerce.dto.request.LoginRequest;
import com.hmanh.ecommerce.dto.request.OtpRequest;
import com.hmanh.ecommerce.dto.response.ApiResponse;
import com.hmanh.ecommerce.dto.response.AuthResponse;
import com.hmanh.ecommerce.repository.VerificationCodeRepository;
import com.hmanh.ecommerce.service.AuthService;
import com.hmanh.ecommerce.service.EmailService;
import com.hmanh.ecommerce.service.SellerService;
import com.hmanh.ecommerce.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller (@RequestBody LoginRequest request) throws Exception {

    String otp = request.getOtp();
    String email =  request.getEmail();

    VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
    if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
        throw  new Exception("Invalid OTP");
    }
    request.setEmail("seller_"+email);
    AuthResponse authResponse = authService.signing(request);
    return ResponseEntity.ok(authResponse);
    }
    @PostMapping
    public Seller createSeller(@RequestBody Seller seller) throws Exception {
        Seller newSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Email verification code";
        String Url = "http://localhost:3000/verify-seller";
        String text = "hi, using this link" + Url;
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text);
        return ResponseEntity.ok(newSeller).getBody();
    }

    @PostMapping("/verifi/{otp}")
    public ResponseEntity<Seller> verificationSellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw  new Exception("Invalid OTP");
        }
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(),otp);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping()
    public ApiResponse<List<Seller>> getSeller(
            @RequestParam(required = false) AccountStatus accountStatus,
            Pageable pageable
    ){
        Page<Seller> page = sellerService.getAllSellers(accountStatus, pageable);

        return ApiResponse.<List<Seller>>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> deletedSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.ok().build();
    }

}
