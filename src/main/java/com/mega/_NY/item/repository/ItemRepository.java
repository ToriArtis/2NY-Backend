package com.mega._NY.item.repository;

import com.mega._NY.item.entity.Item;
import com.mega._NY.item.entity.ItemCategory;
import com.mega._NY.item.entity.ItemColor;
import com.mega._NY.item.entity.ItemSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // 제목으로 검색
    List<Item> findByTitleContainingIgnoreCase(String title);

    // 내용으로 검색
    List<Item> findByContentContainingIgnoreCase(String content);

    // 제목 또는 내용으로 검색
    @Query("SELECT i FROM Item i WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Item> findByTitleOrContentContainingIgnoreCase(@Param("keyword") String keyword);

    //카테고리별 검색
    List<Item> findByCategory(ItemCategory category);

    //높은 가격순
    List<Item> findAllByOrderByPriceDesc();

    // 낮은 가격순
    List<Item> findAllByOrderByPriceAsc();

    // 색상 필터
    List<Item> findByColor(ItemColor color);

    // 사이즈 필터
    List<Item> findBySize(ItemSize size);

    // 색상&사이즈 필터
    List<Item> findByColorAndSize(ItemColor color, ItemSize size);
}