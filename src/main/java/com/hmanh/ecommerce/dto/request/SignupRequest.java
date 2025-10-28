package com.hmanh.ecommerce.dto.request;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {
    private String email;
    private String fullName;
    private String password;
    private String otp;
}
