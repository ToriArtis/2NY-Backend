package com.mega._NY.category.service;

import com.mega._NY.category.dto.CategoryDTO;
import com.mega._NY.category.entity.Category;
import com.mega._NY.category.repository.CategoryRepository;
import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemMapper itemMapper;
    private final ModelMapper modelMapper;

    // 카테고리 등록
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return categoryDTO;
    }

    // 카테고리별 상품 조회
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByCategory(String categoryName) {
        List<Item> items = categoryRepository.findItemsByCategoryName(categoryName);
        return items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }
}