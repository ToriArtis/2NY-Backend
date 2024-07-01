package com.mega._NY.review.service;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import com.mega._NY.review.dto.ReviewDTO;
import com.mega._NY.review.entity.Review;
import com.mega._NY.review.mapper.ReviewMapper;
import com.mega._NY.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    //리뷰 생성
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO){
        Item item = itemRepository.findById(reviewDTO.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Review review = reviewMapper.toEntity(reviewDTO);
        review.setItem(item);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(savedReview);
    }

    //특정 id 리뷰 조회
    @Transactional(readOnly = true)
    public ReviewDTO getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        return reviewMapper.toDTO(review);
    }

    //사용자 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    //특정 상품 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByItemId(Long itemId) {
        return reviewRepository.findByItemItemId(itemId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    //리뷰 수정
    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        review.setContent(reviewDTO.getContent());
        review.setStar(reviewDTO.getStar());
        review.setUpdatedAt(LocalDateTime.now());
        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}
