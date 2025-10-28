package com.hmanh.ecommerce.dto.request;

import com.hmanh.ecommerce.domain.USER_ROLE;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpRequest {
    private String email;
    private USER_ROLE role;
}
