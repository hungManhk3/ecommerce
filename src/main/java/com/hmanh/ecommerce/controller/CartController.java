package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.CartItem;
import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.dto.request.AddItemRequest;
import com.hmanh.ecommerce.dto.response.ApiResponse;
import com.hmanh.ecommerce.dto.response.CartItemResponse;
import com.hmanh.ecommerce.dto.response.CartResponse;
import com.hmanh.ecommerce.exception.ProductException;
import com.hmanh.ecommerce.service.CartItemService;
import com.hmanh.ecommerce.service.CartService;
import com.hmanh.ecommerce.service.ProductService;
import com.hmanh.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<CartResponse> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartResponse cart = cartService.findUserCart(user);
        System.out.println(cart.getUser().getEmail());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addItemToCart(@RequestHeader("Authorization") String jwt, @RequestBody AddItemRequest request) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItemResponse item = cartService.addCartItem(user, request);
        ApiResponse response = new ApiResponse();
        response.setMessage("success");
        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse>  deleteItemFromCart(@PathVariable("cartItemId") Long cartItemId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(),cartItemId);
        ApiResponse response = new ApiResponse();
        response.setMessage("Delete success");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/item/{cartItemId}")
    public ResponseEntity<CartItemResponse> UpdateCartItemHandler(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CartItem cartItem,
            @PathVariable Long cartItemId
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItemResponse updateCartItem = null;
        if (cartItem.getQuantity() > 0){
            updateCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }
        return new ResponseEntity<>(updateCartItem, HttpStatus.OK);
    }

}
