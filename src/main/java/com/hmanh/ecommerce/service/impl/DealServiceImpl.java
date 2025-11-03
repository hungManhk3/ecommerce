package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Deal;
import com.hmanh.ecommerce.Entity.HomeCategory;
import com.hmanh.ecommerce.repository.DealRepository;
import com.hmanh.ecommerce.repository.HomeCategoryRepository;
import com.hmanh.ecommerce.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;
    @Override
    public List<Deal> selectAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory homeCategory = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        Deal newDeal = dealRepository.save(deal);
        newDeal.setCategory(homeCategory);
        newDeal.setDiscount(deal.getDiscount());

        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) {
        Deal existingDeal = dealRepository.findById(id).orElse(null);
        HomeCategory homeCategory = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);

        if (existingDeal != null) {
            if (deal.getDiscount() != null) {
                existingDeal.setDiscount(deal.getDiscount());
            }
            if (deal.getCategory() != null) {
                existingDeal.setCategory(deal.getCategory());
            }
            return dealRepository.save(existingDeal);
        }
        throw new RuntimeException("Deal not found");
    }

    @Override
    public void deleteDeal(Long id) {
        dealRepository.deleteById(id);
    }
}
