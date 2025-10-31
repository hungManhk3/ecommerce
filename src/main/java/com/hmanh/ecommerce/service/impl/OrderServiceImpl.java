package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.*;
import com.hmanh.ecommerce.convert.ConvertToResponse;
import com.hmanh.ecommerce.domain.OrderStatus;
import com.hmanh.ecommerce.domain.PaymentStatus;
import com.hmanh.ecommerce.domain.USER_ROLE;
import com.hmanh.ecommerce.dto.response.OrderItemResponse;
import com.hmanh.ecommerce.dto.response.OrderResponse;
import com.hmanh.ecommerce.exception.OrderNotFoundException;
import com.hmanh.ecommerce.repository.*;
import com.hmanh.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ConvertToResponse convertToResponse;

    @Override
    public Set<OrderResponse> createOrder(User user, Address address, Cart cart) {
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        if (!user.getAddresses().contains(address)) {
            user.getAddresses().add(address);
        }
        Address address1 = addressRepository.save(address);
        // brand 1 => 4 shirt
        // so
        Map<Long , List<CartItem>> itemBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
        Set<OrderResponse> orderResponses = new HashSet<>();
        for (Map.Entry<Long , List<CartItem>> entry : itemBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> cartItems = entry.getValue();
            int totalMrpPrice = cartItems.stream()
                    .mapToInt(item -> item.getMrpPrice() * item.getQuantity())
                    .sum();

            int totalSellingPrice = cartItems.stream()
                    .mapToInt(item -> item.getSellingPrice() * item.getQuantity())
                    .sum();

            int discount = totalMrpPrice - totalSellingPrice;
            int totalItem = cartItems.stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
            Order order = Order.builder()
                    .orderId(generateOrderId())
                    .user(user)
                    .sellerId(sellerId)
                    .address(address1)
                    .totalMrpPrice(totalMrpPrice)
                    .totalSellingPrice(totalSellingPrice)
                    .discount(discount)
                    .totalItem(totalItem)
                    .orderStatus(OrderStatus.PENDING)
                    .paymentStatus(PaymentStatus.PENDING)
                    .orderDate(LocalDateTime.now())
                    .deliveryDate(LocalDateTime.now().plusDays(7))
                    .paymentDetails(PaymentDetails.builder()
                            .paymentStatuss(PaymentStatus.PENDING)
                            .build())
                    .build();
            Order savedOrder = orderRepository.save(order);

            // Tạo order items
            List<OrderItems> orderItemsList = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                OrderItems orderItem = OrderItems.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .size(cartItem.getSize())
                        .quantity(cartItem.getQuantity())
                        .mrpPrice(cartItem.getMrpPrice())
                        .sellingPrice(cartItem.getSellingPrice())
                        .build();

                OrderItems savedOrderItem = orderItemRepository.save(orderItem);
                orderItemsList.add(savedOrderItem);
            }

            savedOrder.setOrderItems(orderItemsList);

            // Cập nhật tồn kho sản phẩm
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                int newQuantity = product.getQuantity() - cartItem.getQuantity();
                product.setQuantity(Math.max(0, newQuantity));
                product.setInStock(newQuantity > 0);
                productRepository.save(product);
            }
            orderResponses.add(convertToResponse.convertToOrderResponse(savedOrder));
        }
        clearCart(cart);
        return orderResponses;

    }

    @Override
    public OrderResponse findOrderById(Long id) {
        Order oder = orderRepository.findByIdWithItems(id).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return convertToResponse.convertToOrderResponse(oder);
    }

    @Override
    public List<OrderResponse> userOrderHistory(Long userId)
    {
        List<Order> orders = orderRepository.findByUserIdWithItems(userId);
        return convertToResponse.convertToOrderResponseList(orders);
    }

    @Override
    public List<OrderResponse> sellerOrders(Long sellerId)
    {
        List<Order> orders = orderRepository.findBySellerIdWithItems(sellerId);
        return convertToResponse.convertToOrderResponseList(orders);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        if (orderStatus == OrderStatus.CANCELLED || orderStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot update order in " + order.getOrderStatus() + " status");
        }
        order.setOrderStatus(orderStatus);
        if (orderStatus == OrderStatus.SHIPPED) {
            order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        }

        Order updatedOrder = orderRepository.save(order);
        return convertToResponse.convertToOrderResponse(updatedOrder);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId, User user) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        if (!order.getUser().getId().equals(user.getId()) &&
                user.getRole() != USER_ROLE.ROLE_ADMIN) {
            throw new SecurityException("You are not authorized to cancel this order");
        }
        if (order.getOrderStatus() != OrderStatus.PENDING &&
                order.getOrderStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel order in " + order.getOrderStatus() + " status");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        // Hoàn lại số lượng tồn kho
        for (OrderItems orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            int newQuantity = product.getQuantity() + orderItem.getQuantity();
            product.setQuantity(newQuantity);
            product.setInStock(true);
            productRepository.save(product);
        }
        Order cancelledOrder = orderRepository.save(order);
        return convertToResponse.convertToOrderResponse(cancelledOrder);
    }

    @Override
    public OrderItemResponse findById(Long id) {
        OrderItems orderItems = orderItemRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return convertToResponse.convertToOrderItemResponse(orderItems);
    }

    private void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotalQuantity(0);
        cart.setTotalMrpPrice(0);
        cart.setTotalSellingPrice(0);
        cart.setDiscount(0);
        cartRepository.save(cart);
    }
    private String generateOrderId() {
        return "ORD" + System.currentTimeMillis() +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
