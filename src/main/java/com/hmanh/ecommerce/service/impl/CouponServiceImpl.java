package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Cart;
import com.hmanh.ecommerce.Entity.Coupon;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.repository.CartRepository;
import com.hmanh.ecommerce.repository.CouponRepository;
import com.hmanh.ecommerce.repository.UserRepository;
import com.hmanh.ecommerce.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        Optional<Cart> cartO = cartRepository.findByUserId(user.getId());
        Cart cart = cartO.get();
        if (coupon == null) throw new Exception("not found coupon");
        if (user.getUsedCoupons().contains(coupon)) {
            throw new Exception("coupon already used");
        }
        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new Exception("coupon min value");
        }
        if (coupon.isActive() && LocalDate.now().isAfter(coupon.getStartDate()) && LocalDate.now().isBefore(coupon.getEndDate())) {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);
            double discountPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercent()) / 100;
            cart.setTotalSellingPrice((int) (cart.getTotalSellingPrice() - discountPrice));
            cart.setCouponCode(code);
            cartRepository.save(cart);
            return cart;
        }
        throw new Exception("coupon has been used");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) throw new Exception("Not found");
        Optional<Cart> cartO = cartRepository.findByUserId(user.getId());
        Cart cart = cartO.get();
        double discountPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercent()) / 100;
        cart.setTotalSellingPrice((int) (cart.getTotalSellingPrice() + discountPrice));
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(() -> new Exception("Not found"));
    }

    @Override
    public List<Coupon> getCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) throws Exception {
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCouponById(Long id) {
        couponRepository.deleteById(id);
    }
}
