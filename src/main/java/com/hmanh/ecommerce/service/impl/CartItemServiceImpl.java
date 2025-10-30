package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.CartItem;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.convert.ConvertToResponse;
import com.hmanh.ecommerce.dto.response.CartItemResponse;
import com.hmanh.ecommerce.repository.CartItemRepository;
import com.hmanh.ecommerce.service.CartItemService;
import jakarta.el.ELException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ConvertToResponse convertToResponse;

    @Override
    public CartItemResponse updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(id);

        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrqPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());
            cartItemRepository.save(item);
            return convertToResponse.convertToCartItemResponse(item);
        }
        else throw new Exception("you cant update this cartItem");
    }

    @Override
    public void removeCartItem(Long userId, Long id) throws Exception {
        CartItem item = findCartItemById(id);

        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            cartItemRepository.delete(item);
        }
        else throw new Exception("you cant delete this cartItem");
    }

    @Override
    public CartItem findCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new ELException("cartItem not found"));
    }
}
