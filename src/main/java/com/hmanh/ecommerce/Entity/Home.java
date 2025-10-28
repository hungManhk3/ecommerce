package com.hmanh.ecommerce.Entity;

import lombok.Data;

import java.util.List;

@Data
public class Home {
    private List<HomeCategory> grid;
    private List<HomeCategory> shopByCategories;
    private List<HomeCategory> dealCategories;
    private List<HomeCategory> electricCategories;
    private List<Deal> deals;
}
