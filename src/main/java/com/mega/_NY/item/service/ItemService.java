package com.mega._NY.item.service;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.dto.ItemTest;
import com.mega._NY.item.dto.ItemWithReviewsDTO;
import com.mega._NY.item.entity.*;
import com.mega._NY.item.mapper.ItemMapper;
import com.mega._NY.item.repository.ItemRepository;
import com.mega._NY.item.repository.SearchRepository;
import com.mega._NY.review.dto.ReviewDTO;
import com.mega._NY.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ReviewService reviewService;
    private final SearchRepository searchRepository;
    private final Path fileStorageLocation;

    public ItemService(ItemRepository itemRepository,
                       ItemMapper itemMapper,
                       ReviewService reviewService,
                       SearchRepository searchRepository,
                       Path fileStorageLocation) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.reviewService = reviewService;
        this.searchRepository = searchRepository;
        this.fileStorageLocation = fileStorageLocation;
    }

    // 상품 추가
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> thumbnailFiles, List<MultipartFile> descriptionImageFiles) throws Exception {
        Item item = itemMapper.toEntity(itemDTO);

        // 썸네일 이미지 업로드
        if (thumbnailFiles != null && !thumbnailFiles.isEmpty()) {
            List<String> thumbnails = uploadImages(thumbnailFiles);
            item.setThumbnail(thumbnails);
        }

        // 상세 이미지 업로드
        if (descriptionImageFiles != null && !descriptionImageFiles.isEmpty()) {
            List<String> descriptionImages = uploadImages(descriptionImageFiles);
            item.setDescriptionImage(descriptionImages);
        }

        Item savedItem = itemRepository.save(item);
        return itemMapper.toDTO(savedItem);
    }

    public ItemTest createItem(ItemTest itemDTO) throws Exception {
        Item item = Item.builder()
                .title(itemDTO.getTitle())
                .content(itemDTO.getContent())
                .price(itemDTO.getPrice())
                .build();

        log.info("1111111111111111111"+item.toString());
        Item savedItem = itemRepository.save(item);
        return itemDTO;
    }

    private List<String> uploadImages(List<MultipartFile> files) throws Exception {
        List<String> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            // 파일 이름에서 경로 구분자 등을 제거
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // 저장할 파일의 전체 경로 생성
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            // 파일을 지정된 위치에 복사 (이미 존재하면 덮어쓰기)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            uploadedFiles.add(fileName);
        }
        return uploadedFiles;
    }

    public Resource loadImages(String filename) throws IOException {
        try {
            // 요청된 파일의 전체 경로 생성
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            // 파일을 리소스로 변환
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + filename);
        }
    }

    // 상품 찾기
    @Transactional(readOnly = true)
    public ItemDTO getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. itemId=" + itemId));
        
        ItemDTO itemDTO = itemMapper.toDTO(item);

        // URL 생성 -> 이미지 파일명 설정
        itemDTO.setThumbnail(item.getThumbnail());
        itemDTO.setDescriptionImage(item.getDescriptionImage());

        return itemDTO;

    }

    // 상품 상세 + 리뷰 조회
    public ItemWithReviewsDTO getItemWithReviews(Long itemId, Pageable pageable) {
        ItemDTO itemDTO = getItem(itemId);
        Page<ReviewDTO> reviews = reviewService.getReviewsByItemId(itemId, pageable);

        return new ItemWithReviewsDTO(itemDTO, reviews);
    }


    // 상품 목록 조회
    @Transactional(readOnly = true)
    public List<ItemDTO> getItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 상품 수정
    @Transactional
    public ItemDTO updateItem(Long itemId, ItemDTO itemDTO, List<MultipartFile> thumbnailFiles, List<MultipartFile> descriptionImageFiles) throws Exception {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. itemId=" + itemId));

        item.setTitle(itemDTO.getTitle());
        item.setContent(itemDTO.getContent());
        item.setPrice(itemDTO.getPrice());
        item.setDiscountPrice(itemDTO.getDiscountPrice());
        item.setDiscountRate(itemDTO.getDiscountRate());
        item.setColor(itemDTO.getColor());
        item.setSize(itemDTO.getSize());
        item.setCategory(itemDTO.getCategory());

        // 썸네일 이미지 업로드
        if (thumbnailFiles != null && !thumbnailFiles.isEmpty()) {
            List<String> thumbnails = uploadImages(thumbnailFiles);
            item.setThumbnail(thumbnails);
        }

        // 상세 이미지 업로드
        if (descriptionImageFiles != null && !descriptionImageFiles.isEmpty()) {
            List<String> descriptionImages = uploadImages(descriptionImageFiles);
            item.setDescriptionImage(descriptionImages);
        }

        Item updatedItem = itemRepository.save(item);
        return itemMapper.toDTO(updatedItem);
    }

    // 상품 삭제
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. itemId=" + itemId));
        itemRepository.delete(item);
    }

    // ItemColor enum을 String으로 변환하는 유틸리티 메서드 (필요한 경우)
    private String colorToString(ItemColor color) {
        return color != null ? color.name() : null;
    }

    // String을 ItemColor enum으로 변환하는 유틸리티 메서드 (필요한 경우)
    private ItemColor stringToColor(String colorString) {
        try {
            return colorString != null ? ItemColor.valueOf(colorString.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 색상입니다: " + colorString);
        }
    }

    // 상품 검색
    @Transactional(readOnly = true)
    public Page<ItemDTO> searchItems(String[] types, String keyword, Pageable pageable) {
        Page<Item> itemPage = searchRepository.searchItems(types, keyword, pageable);
        return itemPage.map(itemMapper::toDTO);
    }

    // 모든 상품 조회
    @Transactional(readOnly = true)
    public Page<ItemDTO> getItems(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAll(pageable);
        return itemPage.map(itemMapper::toDTO);
    }

    // 카테고리별로 상품 조회
    @Transactional(readOnly = true)
    public Page<ItemDTO> getItemsByCategory(ItemCategory category, Pageable pageable) {
        Page<Item> itemPage = itemRepository.findByCategory(category, pageable);
        return itemPage.map(itemMapper::toDTO);
    }

    // 높은 가격순 상품 조회
    @Transactional(readOnly = true)
    public Page<ItemDTO> getItemsByPriceDesc(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAllByOrderByPriceDesc(pageable);
        return itemPage.map(itemMapper::toDTO);
    }

    // 낮은 가격순 상품 조회
    @Transactional(readOnly = true)
    public Page<ItemDTO> getItemsByPriceAsc(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAllByOrderByPriceAsc(pageable);
        return itemPage.map(itemMapper::toDTO);
    }

    // 색상별 상품 필터링
    @Transactional(readOnly = true)
    public Page<ItemDTO> getItemsByColor(ItemColor color, Pageable pageable) {
        Page<Item> itemPage = itemRepository.findByColor(color, pageable);
        return itemPage.map(itemMapper::toDTO);
    }

    // 사이즈별 상품 필터링
    @Transactional(readOnly = true)
    public Page<ItemDTO> getItemsBySize(ItemSize size, Pageable pageable) {
        Page<Item> itemPage = itemRepository.findBySize(size, pageable);
        return itemPage.map(itemMapper::toDTO);
    }

    // 색상, 사이즈 필터링 모두 적용
    @Transactional(readOnly = true)
    public Page<ItemDTO> getItemsByColorAndSize(ItemColor color, ItemSize size, Pageable pageable) {
        Page<Item> itemPage = itemRepository.findByColorAndSize(color, size, pageable);
        return itemPage.map(itemMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<ItemDTO> getItemsByCategoryAndColorAndSize(ItemCategory category, ItemColor color, ItemSize size, Pageable pageable) {
        Page<Item> itemPage = itemRepository.findByCategoryAndColorAndSize(category, color, size, pageable);
        return itemPage.map(itemMapper::toDTO);
    }

}