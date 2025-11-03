package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.HomeCategory;
import com.hmanh.ecommerce.repository.HomeCategoryRepository;
import com.hmanh.ecommerce.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {
    private final HomeCategoryRepository homeCategoryRepository;
    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
        if (homeCategoryRepository.findAll().isEmpty()) {
            return homeCategoryRepository.saveAll(homeCategories);
        }
        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
        HomeCategory existing = homeCategoryRepository.findById(id).orElseThrow(() -> new Exception("not found category"));
        if (existing.getImage() != null ){
            existing.setImage(homeCategory.getImage());
        }
        if (existing.getCategoryId() != null){
            existing.setCategoryId(homeCategory.getCategoryId());
        }
        return homeCategoryRepository.save(existing);
    }

    @Override
    public List<HomeCategory> getHomeCategories() {
        return homeCategoryRepository.findAll();
    }
}
