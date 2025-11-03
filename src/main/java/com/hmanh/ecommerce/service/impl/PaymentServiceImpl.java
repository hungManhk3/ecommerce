package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Order;
import com.hmanh.ecommerce.Entity.PaymentOrder;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.domain.PaymentOrderStatus;
import com.hmanh.ecommerce.domain.PaymentStatus;
import com.hmanh.ecommerce.repository.OrderRepository;
import com.hmanh.ecommerce.repository.PaymentOrderRepository;
import com.hmanh.ecommerce.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;
    private String apiKey = "apikey";
    private String apiSecret = "apiSecret";
    private String stripeSecretKey = "stripeSecretKey";
    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        paymentOrder.setAmount(amount);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception {
        return paymentOrderRepository.findById(paymentOrderId).orElseThrow(() -> new Exception("payment not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByOrderId(String orderId) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(orderId);
        if (paymentOrder == null) {
            throw new Exception("Not found");
        }
        return paymentOrder;
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentOrderId, String paymentLinkId) throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            Payment payment = razorpayClient.payments.fetch(paymentOrderId);
            String status = payment.get("status");
            if (status.equals("captured")){
                Set<Order> orders = paymentOrder.getOrders();
                for (Order order : orders) {
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;
        }
        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) {
        amount = amount * 100;
        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            JSONObject paymentLink = new JSONObject();
            paymentLink.put("amount", amount);
            paymentLink.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLink.put("customer", customer);

            JSONObject notification = new JSONObject();
            notification.put("email", true);
            paymentLink.put("notify", notification);
            paymentLink.put("callback_url", "http://localhost:3000/payment-success/" + orderId);
            paymentLink.put("callback_method", "get");

            PaymentLink paymentLink1 = razorpayClient.paymentLink.create(paymentLink);
            String paymentLinkUrl = paymentLink1.get("short_url");
            String paymentLinkId = paymentLink1.get("id");

            return paymentLink1;
        } catch (RazorpayException e) {
        }
        return null;
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/" + orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel/")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("manhht")
                                                .build()
                                ).build()
                        )
                        .build()
                ).build();
        Session session = Session.create(params);
        return session.getUrl();
    }
}
