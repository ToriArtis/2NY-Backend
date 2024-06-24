package com.mega._NY.item.service;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.mapper.ItemMapper;
import com.mega._NY.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    // 상품 추가
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO) {
        Item item = itemMapper.toEntity(itemDTO);
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDTO(savedItem);
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

        return itemMapper.toDTO(item);
    }

    // 상품 삭제
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. itemId=" + itemId));
        itemRepository.delete(item);
    }
}