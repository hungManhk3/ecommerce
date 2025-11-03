package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.Entity.WishList;
import com.hmanh.ecommerce.dto.response.ProductResponse;
import com.hmanh.ecommerce.service.ProductService;
import com.hmanh.ecommerce.service.UserService;
import com.hmanh.ecommerce.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {
    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<WishList> getWishListByUserId(@RequestHeader String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        WishList wishList = wishListService.getWishListByUserId(user);
        return ResponseEntity.ok(wishList);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishList(@PathVariable Long productId, @RequestHeader String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.getproductById(productId);
        WishList wishList = wishListService.addProductToWishList(user,product);
        return ResponseEntity.ok(wishList);
    }
}
