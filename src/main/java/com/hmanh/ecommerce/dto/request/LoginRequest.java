package com.hmanh.ecommerce.dto.request;

import com.hmanh.ecommerce.domain.USER_ROLE;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    private String email;
    private String otp;
}
