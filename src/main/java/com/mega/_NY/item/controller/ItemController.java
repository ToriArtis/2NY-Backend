package com.mega._NY.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserRoles;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.dto.ItemTest;
import com.mega._NY.item.dto.ItemWithReviewsDTO;
import com.mega._NY.item.entity.ItemCategory;
import com.mega._NY.item.entity.ItemColor;
import com.mega._NY.item.entity.ItemSize;
import com.mega._NY.item.service.ItemService;
import com.mega._NY.review.dto.ReviewDTO;
import com.mega._NY.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private final ReviewService reviewService;

    private boolean isAdmin() {
        User loginUser = userService.getLoginUser();
        boolean isAdmin = loginUser != null && loginUser.getRoleSet().contains(UserRoles.ADMIN);
        log.info("User: {}, Is Admin: {}", loginUser != null ? loginUser.getEmail() : "null", isAdmin);
        return isAdmin;
    }

    // 상품 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemDTO> createItem(
            @RequestPart("itemDTO") ItemDTO itemDTO,
            @RequestPart(value = "thumbnailFiles", required = false) List<MultipartFile> thumbnailFiles,
            @RequestPart(value = "descriptionImageFiles", required = false) List<MultipartFile> descriptionImageFiles) {

        if (!isAdmin()) {
            log.warn("User is not admin");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        log.info("Received itemDTO: {}", itemDTO);
        log.info("Received thumbnailFiles: {}", thumbnailFiles != null ? thumbnailFiles.size() : "null");
        log.info("Received descriptionImageFiles: {}", descriptionImageFiles != null ? descriptionImageFiles.size() : "null");

        try {
            ItemDTO createdItemDTO = itemService.createItem(itemDTO, thumbnailFiles, descriptionImageFiles);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItemDTO);
        } catch (IOException e) {
            log.error("Error uploading files", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Error creating item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 상품 등록
    @PostMapping("/test")
    public ResponseEntity<?> test(
            @RequestBody ItemTest test) {

        if (!isAdmin()) {
            log.warn("User is not admin");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            ItemTest createdItemDTO = itemService.createItem(test);
            return ResponseEntity.ok().body(createdItemDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 이미지 출력
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource file = itemService.loadImages(filename);
            String contentType = "application/octet-stream";
            if (filename.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 상품 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemWithReviewsDTO> getItem(
            @PathVariable Long itemId,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size", defaultValue="10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        ItemDTO itemDTO = itemService.getItem(itemId);

        // 별점 평점 계산
        double averageStar = reviewService.getAvgStarByItemId(itemId);
        itemDTO.setAvgStar(averageStar);

        // 리뷰 조회
        Page<ReviewDTO> reviews = reviewService.getReviewsByItemId(itemId, pageable);

        ItemWithReviewsDTO itemWithReviews = new ItemWithReviewsDTO(itemDTO, reviews);
        return ResponseEntity.ok(itemWithReviews);
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<Page<ItemDTO>> getItems(Pageable pageable) {
        Page<ItemDTO> itemDTOPage = itemService.getItems(pageable);
        return ResponseEntity.ok(itemDTOPage);
    }

    // 상품 수정
    @PutMapping("/{itemId}")
    public ResponseEntity<ItemDTO> updateItem(
            @PathVariable Long itemId,
            @RequestPart(value = "itemDTO", required = false) ItemDTO itemDTO,
            @RequestPart(value = "thumbnailFiles", required = false) List<MultipartFile> thumbnailFiles,
            @RequestPart(value = "descriptionImageFiles", required = false) List<MultipartFile> descriptionImageFiles) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            if (itemDTO == null) {
                return ResponseEntity.badRequest().build();
            }
            ItemDTO updatedItemDTO = itemService.updateItem(itemId, itemDTO, thumbnailFiles, descriptionImageFiles);
            return ResponseEntity.ok(updatedItemDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 상품 삭제
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }

    // 카테고리별 상품 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ItemDTO>> getItemsByCategory(@PathVariable ItemCategory category, Pageable pageable) {
        Page<ItemDTO> itemDTOPage = itemService.getItemsByCategory(category, pageable);
        return ResponseEntity.ok(itemDTOPage);
    }

    // 높은 가격순으로 조회
    @GetMapping("/price/desc")
    public ResponseEntity<Page<ItemDTO>> getItemsByPriceDesc(Pageable pageable) {
        Page<ItemDTO> itemDTOPage = itemService.getItemsByPriceDesc(pageable);
        return ResponseEntity.ok(itemDTOPage);
    }

    // 낮은 가격순으로 조회
    @GetMapping("/price/asc")
    public ResponseEntity<Page<ItemDTO>> getItemsByPriceAsc(Pageable pageable) {
        Page<ItemDTO> itemDTOPage = itemService.getItemsByPriceAsc(pageable);
        return ResponseEntity.ok(itemDTOPage);
    }

    // 색상&사이즈 필터
    @GetMapping("/filter")
    public ResponseEntity<Page<ItemDTO>> getItemsByFilter(
            @RequestParam(required = false) ItemColor color,
            @RequestParam(required = false) ItemSize size,
            Pageable pageable) {

        Page<ItemDTO> itemDTOPage;

        if (color != null && size != null) {
            itemDTOPage = itemService.getItemsByColorAndSize(color, size, pageable);
        } else if (color != null) {
            itemDTOPage = itemService.getItemsByColor(color, pageable);
        } else if (size != null) {
            itemDTOPage = itemService.getItemsBySize(size, pageable);
        } else {
            itemDTOPage = itemService.getItems(pageable);
        }

        return ResponseEntity.ok(itemDTOPage);
    }


}