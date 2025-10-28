package com.hmanh.ecommerce.dto.response;

import com.hmanh.ecommerce.domain.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private boolean authenticated;
    private USER_ROLE role;
}
