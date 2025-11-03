package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Home;
import com.hmanh.ecommerce.Entity.HomeCategory;

import java.util.List;

public interface HomeService {
    Home createHomePageData(List<HomeCategory> allCategories);
}
