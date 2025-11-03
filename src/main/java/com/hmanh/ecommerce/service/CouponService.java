package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.Coupon;
import com.hmanh.ecommerce.Entity.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupon(String code, double orderValue, User user) throws Exception;
    Cart removeCoupon(String code, User user) throws Exception;
    Coupon findCouponById(Long id) throws Exception;
    List<Coupon> getCoupons();
    Coupon createCoupon(Coupon coupon) throws Exception;
    void deleteCouponById(Long id);
}
