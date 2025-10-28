package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.domain.USER_ROLE;
import com.hmanh.ecommerce.dto.request.LoginRequest;
import com.hmanh.ecommerce.dto.request.OtpRequest;
import com.hmanh.ecommerce.dto.request.SignupRequest;
import com.hmanh.ecommerce.dto.response.AuthResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    void sentLoginOtp (OtpRequest request) throws Exception;
    String signup (SignupRequest request) throws Exception;
    AuthResponse signing (LoginRequest request);
    AuthResponse loginSeller (LoginRequest request);
}
