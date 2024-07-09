package com.mega._NY.review.repository;

import com.mega._NY.auth.entity.User;
import com.mega._NY.item.entity.Item;
import com.mega._NY.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByItemItemId(Long itemId, Pageable pageable);
    List<Review> findByItemItemId(Long itemId);
    Page<Review> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserAndItem(User user, Item item);
}