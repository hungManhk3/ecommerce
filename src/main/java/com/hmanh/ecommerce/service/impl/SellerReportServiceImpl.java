package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.Entity.SellerReport;
import com.hmanh.ecommerce.repository.SellerReportRepository;
import com.hmanh.ecommerce.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {
    private final SellerReportRepository sellerReportRepository;
    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sellerReport = sellerReportRepository.findBySellerId(seller.getId());
        if (sellerReport == null) {
            sellerReport = SellerReport.builder()
                    .seller(seller)
                    .build();
            return sellerReportRepository.save(sellerReport);
        }
        return sellerReport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
