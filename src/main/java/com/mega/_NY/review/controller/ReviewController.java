package com.mega._NY.review.controller;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserRoles;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.review.dto.ReviewDTO;
import com.mega._NY.review.service.ReviewService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    private boolean isUser() {
        User loginUser = userService.getLoginUser();
        return loginUser.getRoleSet().contains(UserRoles.USER);
    }

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
    public ResponseEntity<Page<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId, Pageable pageable) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId, pageable);
        return ResponseEntity.ok(reviews);
    }

    //특정 상품 리뷰 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByItemId(
            @PathVariable Long itemId,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size", defaultValue="10") int size
            ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDTO> reviews = reviewService.getReviewsByItemId(itemId, pageable);
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
