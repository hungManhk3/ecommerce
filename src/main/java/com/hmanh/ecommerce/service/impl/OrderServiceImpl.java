package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.*;
import com.hmanh.ecommerce.domain.OrderStatus;
import com.hmanh.ecommerce.domain.PaymentStatus;
import com.hmanh.ecommerce.repository.AddressRepository;
import com.hmanh.ecommerce.repository.OrderItemRepository;
import com.hmanh.ecommerce.repository.OrderRepository;
import com.hmanh.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address address, Cart cart) {
        if (!user.getAddresses().contains(address)) {
            user.getAddresses().add(address);
        }
        Address address1 = addressRepository.save(address);
        // brand 1 => 4 shirt
        // so
        Map<Long , List<CartItem>> itemBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
        Set<Order> orders = new HashSet<>();
        for (Map.Entry<Long , List<CartItem>> entry : itemBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> cartItems = entry.getValue();
            int totalOrderPrice = cartItems.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
            Order createOrder = new Order();
            createOrder.setUser(user);
            createOrder.setAddress(address1);
            createOrder.setSellerId(sellerId);
            createOrder.setTotalMrpPrice(totalOrderPrice);
            createOrder.setTotalSellingPrice(totalOrderPrice);
            createOrder.setTotalItem(totalItem);
            createOrder.setOrderStatus(OrderStatus.PENDING);
            createOrder.getPaymentDetails().setPaymentStatuss(PaymentStatus.PENDING);

            Order order = orderRepository.save(createOrder);
            orders.add(order);

            List<OrderItems> orderItems = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                OrderItems orderItem = new OrderItems();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setMrpPrice(cartItem.getMrpPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSize(cartItem.getSize());
                orderItem.setUserId(cartItem.getUserId());
                orderItem.setSellingPrice(cartItem.getSellingPrice());

                order.getOrderItems().add(orderItem);

                OrderItems saveOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(saveOrderItem);
            }
        }
        return orders;

    }

    @Override
    public Order findOrderById(Long id) {
        return null;
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return List.of();
    }

    @Override
    public List<Order> sellerOrders(Long sellerId) {
        return List.of();
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        return null;
    }

    @Override
    public Order cancelOrder(Long orderId, User user) {
        return null;
    }
}
