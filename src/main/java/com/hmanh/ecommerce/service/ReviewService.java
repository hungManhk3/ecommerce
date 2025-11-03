package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Review;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.dto.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest request, User user, Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long  reviewId,String reviewText,  double reviewRating, Long userId) throws Exception;
    void deleteReviewByReviewId(Long reviewId, Long userId) throws Exception;
    Review getReviewById(Long reviewId) throws Exception;
}
