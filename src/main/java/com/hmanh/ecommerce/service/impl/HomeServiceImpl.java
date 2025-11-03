package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Deal;
import com.hmanh.ecommerce.Entity.Home;
import com.hmanh.ecommerce.Entity.HomeCategory;
import com.hmanh.ecommerce.domain.HomeCategorySection;
import com.hmanh.ecommerce.repository.DealRepository;
import com.hmanh.ecommerce.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final DealRepository dealRepository;
    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.GRID)
                .toList();
        List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(homeCategory -> homeCategory.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES)
                .toList();
        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(homeCategory -> homeCategory.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES)
                .toList();
        List<HomeCategory> dealCategories = allCategories.stream()
                .filter(homeCategory -> homeCategory.getSection() == HomeCategorySection.DEALS)
                .toList();
        List<Deal> createDeals = new ArrayList<>();
        if (dealRepository.findAll().isEmpty()){
            List<Deal> deals = allCategories.stream()
                    .filter(homeCategory -> homeCategory.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .toList();
            createDeals =  dealRepository.saveAll(deals);

        }else createDeals = dealRepository.findAll();
        Home home = Home.builder()
                .grid(gridCategories)
                .shopByCategories(shopByCategories)
                .deals(createDeals)
                .electricCategories(electricCategories)
                .dealCategories(dealCategories)
                .build();
        return home;
    }
}
