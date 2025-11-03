package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.Entity.WishList;
import com.hmanh.ecommerce.repository.WishListRepository;
import com.hmanh.ecommerce.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;

    @Override
    public WishList createWishList(User user) {
        WishList wishList = WishList.builder()
                .user(user)
                .build();

        return wishListRepository.save(wishList);
    }

    @Override
    public WishList getWishListByUserId(User user) {
        WishList wishList = wishListRepository.findByUserId(user.getId());
        if (wishList == null) {
            wishList = createWishList(user);
        }

        return wishList;
    }

    @Override
    public WishList addProductToWishList(User user, Product product) {
        WishList wishList = wishListRepository.findByUserId(user.getId());
        if (wishList.getProducts().contains(product)) {
            wishList.getProducts().remove(product);
        }
        else wishList.getProducts().add(product);

        return wishListRepository.save(wishList);
    }
}
