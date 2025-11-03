package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Home;
import com.hmanh.ecommerce.Entity.HomeCategory;
import com.hmanh.ecommerce.service.HomeCategoryService;
import com.hmanh.ecommerce.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomePageData(@RequestBody List<HomeCategory> allCategories) {
        List<HomeCategory> homeCategories = homeCategoryService.createCategories(allCategories);
        Home home = homeService.createHomePageData(homeCategories);
        return ResponseEntity.ok(home);
    }

    @GetMapping("/admin/home-categories")
    public ResponseEntity<List<HomeCategory>> getAllCategories() {
        List<HomeCategory> homeCategories = homeCategoryService.getHomeCategories();
        return ResponseEntity.ok(homeCategories);
    }

    @PatchMapping("/home-categories/{id}")
    public ResponseEntity<HomeCategory> updateHomePageData(@PathVariable Long id, @RequestBody HomeCategory homeCategory) throws Exception {
        HomeCategory up = homeCategoryService.updateHomeCategory(homeCategory, id);
        return ResponseEntity.ok(up);
    }
}
