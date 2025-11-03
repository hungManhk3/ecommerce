package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Deal;

import java.util.List;

public interface DealService {
    List<Deal> selectAllDeals();
    Deal createDeal(Deal deal);
    Deal updateDeal(Deal deal, Long id);
    void deleteDeal(Long id);
}
