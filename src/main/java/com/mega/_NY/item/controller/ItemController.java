package com.mega._NY.item.controller;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.ItemCategory;
import com.mega._NY.item.entity.ItemColor;
import com.mega._NY.item.entity.ItemSize;
import com.mega._NY.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ItemDTO> createItem(
            @ModelAttribute ItemDTO itemDTO,
            @RequestParam(value = "thumbnailFiles", required = false) List<MultipartFile> thumbnailFiles,
            @RequestParam(value = "descriptionImageFiles", required = false) List<MultipartFile> descriptionImageFiles) {
        try {
            ItemDTO createdItemDTO = itemService.createItem(itemDTO, thumbnailFiles, descriptionImageFiles);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItemDTO);
        } catch (Exception e) {
            // 이미지 업로드 실패 시 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 상품 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long itemId) {
        ItemDTO itemDTO = itemService.getItem(itemId);
        return ResponseEntity.ok(itemDTO);
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItems() {
        List<ItemDTO> itemDTOList = itemService.getItems();
        return ResponseEntity.ok(itemDTOList);
    }

    // 상품 수정
    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long itemId, @RequestBody ItemDTO itemDTO) {
        ItemDTO updatedItemDTO = itemService.updateItem(itemId, itemDTO);
        return ResponseEntity.ok(updatedItemDTO);
    }

    // 상품 삭제
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
    
    // 카테고리별 상품 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ItemDTO>> getItemsByCategory(@PathVariable ItemCategory category) {
        List<ItemDTO> items = itemService.getItemsByCategory(category);
        return ResponseEntity.ok(items);
    }

    // 높은 가격순으로 조회
    @GetMapping("/price/desc")
    public ResponseEntity<List<ItemDTO>> getItemsByPriceDesc() {
        List<ItemDTO> itemDTOList = itemService.getItemsByPriceDesc();
        return ResponseEntity.ok(itemDTOList);
    }

    // 낮은 가격순으로 조회
    @GetMapping("/price/asc")
    public ResponseEntity<List<ItemDTO>> getItemsByPriceAsc() {
        List<ItemDTO> itemDTOList = itemService.getItemsByPriceAsc();
        return ResponseEntity.ok(itemDTOList);
    }


    // 색상&사이즈 필터
    @GetMapping("/filter")
    public ResponseEntity<List<ItemDTO>> getItemsByFilter(
            @RequestParam(required = false) ItemColor color,
            @RequestParam(required = false) ItemSize size) {

        List<ItemDTO> itemDTOList;

        if (color != null && size != null) {
            itemDTOList = itemService.getItemsByColorAndSize(color, size);
        } else if (color != null) {
            itemDTOList = itemService.getItemsByColor(color);
        } else if (size != null) {
            itemDTOList = itemService.getItemsBySize(size);
        } else {
            itemDTOList = itemService.getItems();
        }

        return ResponseEntity.ok(itemDTOList);
    }
}