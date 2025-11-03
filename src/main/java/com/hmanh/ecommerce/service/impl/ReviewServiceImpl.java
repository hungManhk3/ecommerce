package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Review;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.dto.request.CreateReviewRequest;
import com.hmanh.ecommerce.repository.ProductRepository;
import com.hmanh.ecommerce.repository.ReviewRepository;
import com.hmanh.ecommerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    public Review createReview(CreateReviewRequest request, User user, Product product) {
        Review review  = Review.builder()
                .reviewText(request.getReviewText())
                .user(user)
                .product(product)
                .rating(request.getReviewRating())
                .productImages(product.getImages())
                .build();

        product.getReviews().add(review);
        productRepository.save(product);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double reviewRating, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if (review.getUser().getId().equals(userId)) {
            review.setReviewText(reviewText);
            review.setRating(reviewRating);
            return reviewRepository.save(review);
        }
        throw new Exception("you cant update review");
    }

    @Override
    public void deleteReviewByReviewId(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if (review.getUser().getId().equals(userId)) throw new Exception("you cant delete review");
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new Exception("Not found"));
    }
}
