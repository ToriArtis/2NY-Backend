package com.mega._NY.review.repository;

import com.mega._NY.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByItemItemId(Long itemId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
}