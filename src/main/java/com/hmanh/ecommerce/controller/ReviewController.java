package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.Entity.Product;
import com.hmanh.ecommerce.Entity.Review;
import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.dto.request.CreateReviewRequest;
import com.hmanh.ecommerce.dto.response.ApiResponse;
import com.hmanh.ecommerce.service.ProductService;
import com.hmanh.ecommerce.service.ReviewService;
import com.hmanh.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewByProductId(@PathVariable Long productId){
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<Review> createReview(@PathVariable Long productId, @RequestBody CreateReviewRequest request, @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.getproductById(productId);
        Review review = reviewService.createReview(request,user,product);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @RequestBody CreateReviewRequest request, @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Review updateReview = reviewService.updateReview(reviewId, request.getReviewText(), request.getReviewRating(), user.getId());

        return ResponseEntity.ok(updateReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Object>> deleteReview(@PathVariable Long reviewId, @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReviewByReviewId(reviewId, user.getId());
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("review deleted successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
