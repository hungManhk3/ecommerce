package com.hmanh.ecommerce.Entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
}
