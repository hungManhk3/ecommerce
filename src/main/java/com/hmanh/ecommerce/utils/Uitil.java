package com.hmanh.ecommerce.utils;

public class Uitil {
    public static Integer calculateDiscountPercentage(double mrpPrice, double sellingPrice) {
        if (mrpPrice <= 0) {
            throw new IllegalArgumentException("Than 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int)discountPercentage;
    }
}
