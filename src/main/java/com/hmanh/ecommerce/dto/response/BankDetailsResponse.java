package com.hmanh.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsResponse {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
}