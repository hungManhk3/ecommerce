package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.Entity.WishList;

public interface WishListService {
    WishList createWishList(User user);
    WishList getWishListByUserId(User user);
    WishList addProductToWishList(User user, Product product);
}
