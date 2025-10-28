package com.hmanh.ecommerce.Entity;

import com.hmanh.ecommerce.domain.PaymentStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetails {
    private String paymentId;
    private String paymentLinkId;
    private String paymentLinkReferenceId;
    private String paymentLinkStatus;
    private String paymentIdZWSP;
    private PaymentStatus paymentStatuss;
}
