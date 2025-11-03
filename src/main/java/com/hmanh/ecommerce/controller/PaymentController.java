package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.PaymentOrder;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.dto.response.ApiResponse;
import com.hmanh.ecommerce.service.PaymentService;
import com.hmanh.ecommerce.service.UserService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;

//    @GetMapping("/{paymentId}")
//    public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable String paymentId, @RequestParam String paymentLinkId, @RequestHeader("Authorization") String jwt) throws Exception {
//        User user = userService.findUserByJwtToken(jwt);
//    }
// 11:34:49
}
