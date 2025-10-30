package com.hmanh.ecommerce.dto.response;

import com.hmanh.ecommerce.domain.AccountStatus;
import com.hmanh.ecommerce.domain.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {
    private Long id;
    private String sellerName;
    private String mobile;
    private String email;
    private BusinessDetailsResponse businessDetails;
    private BankDetailsResponse bankDetails;
    private AddressResponse pickupAddress;
    private String GSTIN;
    private USER_ROLE role;
    private boolean emailVerified;
    private boolean deleted;
    private AccountStatus status;
}