package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.Entity.VerificationCode;
import com.hmanh.ecommerce.domain.USER_ROLE;
import com.hmanh.ecommerce.dto.request.LoginRequest;
import com.hmanh.ecommerce.dto.request.OtpRequest;
import com.hmanh.ecommerce.dto.request.SignupRequest;
import com.hmanh.ecommerce.dto.response.AuthResponse;
import com.hmanh.ecommerce.exception.AppException;
import com.hmanh.ecommerce.exception.ErrorCode;
import com.hmanh.ecommerce.repository.CartRepository;
import com.hmanh.ecommerce.repository.SellerRepository;
import com.hmanh.ecommerce.repository.UserRepository;
import com.hmanh.ecommerce.repository.VerificationCodeRepository;
import com.hmanh.ecommerce.service.AuthService;
import com.hmanh.ecommerce.service.EmailService;
import com.hmanh.ecommerce.utils.OtpUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomerServiceImpl customerService;
    private final SellerRepository sellerRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;
    private static final String SELLER_PERFIX = "seller_";

    @Override
    public void sentLoginOtp(OtpRequest request) throws Exception {
        String otp = OtpUtil.generateOtp();
        String subject = "Xác minh tài khoản - Mã OTP";
        String text = """
        <html>
            <body style="font-family: Arial, sans-serif; color: #333;">
                <p>Chào bạn,</p>
                <p>Mã OTP của bạn là:</p>
                <p style="font-size: 24px; color: red; font-weight: bold;">%s</p>
                <p>Vui lòng nhập mã này để hoàn tất xác minh.</p>
                <br/>
                <p>Trân trọng,<br/>MH</p>
            </body>
        </html>
        """.formatted(otp);
        if (emailService.sendVerificationOtpEmail(request.getEmail(), otp, subject, text)){
            VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
            if (verificationCode == null){
                verificationCode = new VerificationCode();
            }
            verificationCode.setOtp(otp);
            verificationCode.setEmail(request.getEmail());
            if (request.getRole().equals(USER_ROLE.ROLE_CUSTOMER)){
                User user = userRepository.findByEmail(request.getEmail());
                if (user != null){
                    verificationCode.setUser(user);
                    verificationCode.setSeller(null);
                }
            } else if (request.getRole().equals(USER_ROLE.ROLE_SELLER)){
                Seller seller = sellerRepository.findByEmail(request.getEmail());
                if (seller != null){
                    verificationCode.setSeller(seller);
                    verificationCode.setUser(null);
                }
            }
            verificationCodeRepository.save(verificationCode);
        }
    }

    @Override
    public String signup(SignupRequest request) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
            throw new Exception("Wrong");
        }
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null){
            User newUser = User.builder()
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .role(USER_ROLE.ROLE_CUSTOMER)
                    .mobile("0356966666")
                    .password(passwordEncoder.encode(request.getOtp()))
                    .build();
            user = userRepository.save(newUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.name()));
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest request) {
        String username = request.getEmail();
        String otp = request.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = generateToken(authentication);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        return AuthResponse.builder()
                .token(token)
                .message("Login Successful")
                .authenticated(true)
                .role(USER_ROLE.valueOf(roleName))
                .build();
    }

    @Override
    public AuthResponse loginSeller(LoginRequest request) {
        return null;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customerService.loadUserByUsername(username);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(userDetails.getUsername());
        if (verificationCode == null ||  !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("wrong email or password");
        }
        return new  UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String generateToken(Authentication authentication) {
        //tao token = thu vien nimbus
        // jwsObject can 2 param header: thuat toan ma hoa la gi ; payload data trong body thi can 1 claim
        String username = authentication.getName();
        String scope = buildScope(authentication);
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("manhht")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope" ,scope)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(SINGER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.EXCEPTION);
        }
    }
    private String buildScope(Authentication authentication) {
        // Lấy danh sách quyền từ Authentication
        Set<String> scope = new HashSet<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            scope.add(authority.getAuthority());
        }
        return String.join(" ", scope);
    }

    private VerificationCode verificationCode(String email,  String otp) {
        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        return verificationCodeRepository.save(verificationCode);
    }
}
