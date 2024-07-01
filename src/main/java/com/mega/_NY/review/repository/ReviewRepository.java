package com.mega._NY.review.repository;

import com.mega._NY.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByItemItemId(Long itemId);
    List<Review> findByUserId(Long userId);
}