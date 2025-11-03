package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Deal;
import com.hmanh.ecommerce.dto.response.ApiResponse;
import com.hmanh.ecommerce.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;

    @PostMapping()
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {
        Deal createDeal = dealService.createDeal(deal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createDeal);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) {
        Deal updateDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(updateDeal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) {
        dealService.deleteDeal(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Deal deleted successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping()
    public ResponseEntity<List<Deal>> getDeal(@RequestParam Long id) {
        List<Deal> deals = dealService.selectAllDeals();
        return ResponseEntity.ok(deals);
    }
}
