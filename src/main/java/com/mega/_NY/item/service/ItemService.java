package com.mega._NY.item.service;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.*;
import com.mega._NY.item.mapper.ItemMapper;
import com.mega._NY.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

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

    private List<String> uploadImages(List<MultipartFile> files) throws Exception {
        List<String> uploadedFiles = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                if (originalName != null && !originalName.isEmpty()) {
                    // 파일 이름 생성
                    String fileName = System.currentTimeMillis() + "_" + originalName;
                    // 파일 저장 경로
                    String savePath = System.getProperty("user.dir") + "/src/main/resources/static/images/";
                    // 저장 경로 없으면 디렉토리 생성
                    if (!new File(savePath).exists()) {
                        new File(savePath).mkdir();
                    }
                    String filePath = savePath + fileName;
                    file.transferTo(new File(filePath));
                    uploadedFiles.add(fileName);
                }
            }
        }

        return uploadedFiles;
    }

    // 상품 찾기
    @Transactional(readOnly = true)
    public ItemDTO getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. itemId=" + itemId));
        return itemMapper.toDTO(item);
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
    public ItemDTO updateItem(Long itemId, ItemDTO itemDTO) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. itemId=" + itemId));

        item.setTitle(itemDTO.getTitle());
        item.setContent(itemDTO.getContent());
        item.setThumbnail(itemDTO.getThumbnail());
        item.setDescriptionImage(itemDTO.getDescriptionImage());
        item.setPrice(itemDTO.getPrice());
        item.setDiscountPrice(itemDTO.getDiscountPrice());
        item.setDiscountRate(itemDTO.getDiscountRate());
        item.setSales(itemDTO.getSales());
        item.setColor(itemDTO.getColor());  // ItemColor enum을 직접 설정
        item.setSize(itemDTO.getSize());
        item.setCategory(itemDTO.getCategory());

        return itemMapper.toDTO(item);
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

    // 제목으로 검색
    @Transactional(readOnly = true)
    public List<ItemDTO> searchByTitle(String title) {
        return itemRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 내용으로 검색
    @Transactional(readOnly = true)
    public List<ItemDTO> searchByContent(String content) {
        return itemRepository.findByContentContainingIgnoreCase(content).stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 제목 또는 내용으로 검색
    @Transactional(readOnly = true)
    public List<ItemDTO> searchByTitleOrContent(String keyword) {
        return itemRepository.findByTitleOrContentContainingIgnoreCase(keyword).stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 카테고리별 상품 조회
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByCategory(ItemCategory category) {
        List<Item> items = itemRepository.findByCategory(category);
        return items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 높은 가격순 조회
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByPriceDesc() {
        List<Item> items = itemRepository.findAllByOrderByPriceDesc();
        return items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 낮은 가격순 조회
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByPriceAsc() {
        List<Item> items = itemRepository.findAllByOrderByPriceAsc();
        return items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 색상 필터
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByColor(ItemColor color) {
        List<Item> items = itemRepository.findByColor(color);
        return items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 사이즈 필터
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsBySize(ItemSize size) {
        List<Item> items = itemRepository.findBySize(size);
        return items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 색상&사이즈 필터
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByColorAndSize(ItemColor color, ItemSize size) {
        List<Item> items = itemRepository.findByColorAndSize(color, size);
        return items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }
}