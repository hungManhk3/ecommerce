package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Order;
import com.hmanh.ecommerce.Entity.PaymentOrder;
import com.hmanh.ecommerce.Entity.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception;
    PaymentOrder getPaymentOrderByOrderId(String orderId) throws Exception;
    Boolean ProceedPaymentOrder (PaymentOrder paymentOrder, String paymentOrderId, String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId);
    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
