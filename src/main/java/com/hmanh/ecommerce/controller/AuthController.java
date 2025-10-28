package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.domain.USER_ROLE;
import com.hmanh.ecommerce.dto.request.LoginRequest;
import com.hmanh.ecommerce.dto.request.OtpRequest;
import com.hmanh.ecommerce.dto.request.SignupRequest;
import com.hmanh.ecommerce.dto.response.ApiResponse;
import com.hmanh.ecommerce.dto.response.AuthResponse;
import com.hmanh.ecommerce.repository.UserRepository;
import com.hmanh.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(@RequestBody SignupRequest request) throws Exception {
        String token = authService.signup(request);
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .message("register successfully")
                .role(USER_ROLE.ROLE_CUSTOMER)
                .authenticated(true)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/sent/otp")
    public ResponseEntity<?> sentOtpHandler(@RequestBody OtpRequest request) throws Exception {
        authService.sentLoginOtp(request);
        return ResponseEntity.ok(ApiResponse.builder()
                                .message("otp sent successfully")
                                .build()
        );
    }
    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) throws Exception {
        AuthResponse authResponse =authService.signing(request);
        return ResponseEntity.ok(authResponse);
    }
}
