package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Seller;
import com.hmanh.ecommerce.Entity.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
