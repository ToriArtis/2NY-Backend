package com.mega._NY.review.controller;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.review.dto.ReviewDTO;
import com.mega._NY.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 생성
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO){
         ReviewDTO createdReview = reviewService.createReview(reviewDTO);
         return ResponseEntity.ok(reviewDTO);
    }

    //특정 id 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId) {
        ReviewDTO review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }
    //사용자 모든 리뷰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
    //특정 상품 리뷰 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByItemId(@PathVariable Long itemId){
        List<ReviewDTO> reviews = reviewService.getReviewsByItemId(itemId);
        return ResponseEntity.ok(reviews);
    }

    //리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId,
                                                   @RequestBody ReviewDTO reviewDTO){
        ReviewDTO updateReviewDTO = reviewService.updateReview(reviewId, reviewDTO);
        return ResponseEntity.ok(updateReviewDTO);
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();

    }
}
