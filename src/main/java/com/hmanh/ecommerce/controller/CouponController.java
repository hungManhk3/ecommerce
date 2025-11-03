package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.Coupon;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.repository.CouponRepository;
import com.hmanh.ecommerce.service.CouponService;
import com.hmanh.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;
    private final UserService userService;

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) throws Exception{
        Coupon createCoupon = couponService.createCoupon(coupon);
        return new ResponseEntity<>(createCoupon, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) throws Exception{
        couponService.deleteCouponById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons() throws Exception{
        List<Coupon> coupons = couponService.getCoupons();
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }
    @GetMapping("/admin/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) throws Exception{
        Coupon coupon = couponService.findCouponById(id);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }
    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(@RequestParam String apply, @RequestParam String code, @RequestParam double orderValue, @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;
        if (apply.equals("true")) {
            cart = couponService.applyCoupon(code, orderValue, user);
        }
        else {
            cart = couponService.removeCoupon(code,user );
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
