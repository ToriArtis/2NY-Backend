package com.mega._NY.item.repository;

import com.mega._NY.item.entity.Item;
import com.mega._NY.item.entity.ItemCategory;
import com.mega._NY.item.entity.ItemColor;
import com.mega._NY.item.entity.ItemSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 카테고리별 검색
    Page<Item> findByCategory(ItemCategory category, Pageable pageable);

    // 높은 가격순
    Page<Item> findAllByOrderByPriceDesc(Pageable pageable);

    // 낮은 가격순
    Page<Item> findAllByOrderByPriceAsc(Pageable pageable);

    // 색상 필터
    Page<Item> findByColor(ItemColor color, Pageable pageable);

    // 사이즈 필터
    Page<Item> findBySize(ItemSize size, Pageable pageable);

    // 색상&사이즈 필터
    Page<Item> findByColorAndSize(ItemColor color, ItemSize size, Pageable pageable);
}