package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.CartItem;
import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.repository.CartItemRepository;
import com.hmanh.ecommerce.repository.CartRepository;
import com.hmanh.ecommerce.repository.UserRepository;
import com.hmanh.ecommerce.service.CartService;
import com.hmanh.ecommerce.utils.Uitil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem isExits = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
        if (isExits==null){
            int totalPrice = quantity * product.getSellingPrice();
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .size(size)
                    .userId(user.getId())
                    .sellingPrice(totalPrice)
                    .cart(cart)
                    .build();
            CartItem savedCartItem = cartItemRepository.save(cartItem);
            cart.getCartItems().add(savedCartItem);

            cartRepository.save(cart);

            return isExits;
        }
        return null;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        int totalPrice = 0;
        int totalDiscountPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalSellingPrice(totalDiscountPrice);
        cart.setTotalQuantity(totalItem);
//        cart.setDiscount(Uitil.calculateDiscountPercentage(totalPrice, totalDiscountPrice));
        return cartRepository.save(cart);
    }

    @Override
    public Cart findProductCart(Product product) {
        return null;
    }
}
