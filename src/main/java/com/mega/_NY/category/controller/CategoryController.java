package com.mega._NY.category.controller;

import com.mega._NY.category.dto.CategoryDTO;
import com.mega._NY.category.service.CategoryService;
import com.mega._NY.item.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //카테고리 등록
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategoryDTO = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategoryDTO);
    }

    // 카테고리별 상품 조회
    @GetMapping("/{categoryName}/items")
    public ResponseEntity<List<ItemDTO>> getItemsByCategory(@PathVariable String categoryName) {
        List<ItemDTO> itemDTOList = categoryService.getItemsByCategory(categoryName);
        return ResponseEntity.ok(itemDTOList);
    }

}