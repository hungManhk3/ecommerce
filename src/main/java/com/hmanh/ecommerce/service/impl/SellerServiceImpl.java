package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Address;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.domain.AccountStatus;
import com.hmanh.ecommerce.domain.USER_ROLE;
import com.hmanh.ecommerce.exception.SellerException;
import com.hmanh.ecommerce.repository.AddressRepository;
import com.hmanh.ecommerce.repository.SellerRepository;
import com.hmanh.ecommerce.service.AuthService;
import com.hmanh.ecommerce.service.SellerService;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception{
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }
        JWSVerifier jwsVerifier = new MACVerifier(SINGER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        boolean verified = signedJWT.verify(jwsVerifier);
        if (!verified) {
            throw new Exception("JWT signature verification failed");
        }
        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expTime.before(new Date())) {
            throw new Exception("JWT token has expired");
        }
        String email = signedJWT.getJWTClaimsSet().getSubject();
        if (email == null) {
            throw new Exception("JWT token does not contain subject");
        }
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExists = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExists != null) {
            throw new Exception("Seller already exist");
        }
        Address savedAddress = addressRepository.save(seller.getPickupAddress());
        Seller newSeller = Seller.builder()
                .email(seller.getEmail())
                .password(passwordEncoder.encode(seller.getPassword()))
                .sellerName(seller.getSellerName())
                .pickupAddress(savedAddress)
                .GSTIN(seller.getGSTIN())
                .role(USER_ROLE.ROLE_SELLER)
                .mobile(seller.getMobile())
                .bankDetails(seller.getBankDetails())
                .businessDetails(seller.getBusinessDetails())
                .build();
        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException{
        Seller seller = sellerRepository.findSellerById(id);
        if (seller == null) {
            throw new SellerException("seller not found");
        }
        return seller;
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("seller not found");
        }
        return seller;
    }

    @Override
    public Page<Seller> getAllSellers(AccountStatus status, Pageable pageable) {
        return sellerRepository.searchSellers(status, pageable);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) {
        Seller existingSeller = sellerRepository.findById(id).orElseThrow(() -> new RuntimeException("seller not found"));

        if (seller.getSellerName() != null){
            existingSeller.setSellerName(seller.getSellerName());
        }
        if (seller.getEmail() != null){
            existingSeller.setEmail(seller.getEmail());
        }
        if (seller.getMobile() != null){
            existingSeller.setMobile(seller.getMobile());
        }
        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null){
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }
        if (seller.getBankDetails() != null
            && seller.getBankDetails().getAccountNumber() != null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountHolderName() != null
        ){
            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
        }
        if (seller.getPickupAddress() != null
            && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null
                && seller.getPickupAddress().getAddress() != null
        ){
            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
        }
        if (seller.getGSTIN() != null) {
            existingSeller.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller =  this.getSellerById(id);
        seller.setDeleted(true);
        sellerRepository.save(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception {
        Seller seller = getSellerById(id);
        seller.setStatus(status);
        return sellerRepository.save(seller);
    }
}
