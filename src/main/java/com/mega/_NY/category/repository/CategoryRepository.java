package com.mega._NY.category.repository;

import com.mega._NY.category.entity.Category;
import com.mega._NY.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByItemItemId(Long itemId);

    @Query("SELECT c.item FROM Category c WHERE c.categoryName = :categoryName")
    List<Item> findItemsByCategoryName(@Param("categoryName") String categoryName);
}