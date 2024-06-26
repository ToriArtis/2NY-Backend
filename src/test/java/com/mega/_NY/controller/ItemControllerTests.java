package com.mega._NY.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDTO itemDTO;

    @BeforeEach
    void setUp() {
        itemDTO = new ItemDTO();
        itemDTO.setTitle("Test Item");
        itemDTO.setContent("Test Content");
        itemDTO.setThumbnail("test_thumbnail.jpg");
        itemDTO.setDescriptionImage("test_description.jpg");
        itemDTO.setPrice(1000);
        itemDTO.setDiscountPrice(900);
        itemDTO.setDiscountRate(10);
        itemDTO.setSales(0);
    }

    @Test
    void createItem() throws Exception {
        String content = objectMapper.writeValueAsString(itemDTO);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(itemDTO.getTitle()))
                .andExpect(jsonPath("$.content").value(itemDTO.getContent()))
                .andExpect(jsonPath("$.price").value(itemDTO.getPrice()))
                .andExpect(jsonPath("$.discountPrice").value(itemDTO.getDiscountPrice()))
                .andExpect(jsonPath("$.discountRate").value(itemDTO.getDiscountRate()));
    }

    @Test
    void getItem() throws Exception {
        Item savedItem = itemRepository.save(Item.builder()
                .title(itemDTO.getTitle())
                .content(itemDTO.getContent())
                .thumbnail(itemDTO.getThumbnail())
                .descriptionImage(itemDTO.getDescriptionImage())
                .price(itemDTO.getPrice())
                .discountPrice(itemDTO.getDiscountPrice())
                .discountRate(itemDTO.getDiscountRate())
                .sales(itemDTO.getSales())
                .build());

        mockMvc.perform(get("/items/{itemId}", savedItem.getItemId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(itemDTO.getTitle()))
                .andExpect(jsonPath("$.content").value(itemDTO.getContent()))
                .andExpect(jsonPath("$.price").value(itemDTO.getPrice()))
                .andExpect(jsonPath("$.discountPrice").value(itemDTO.getDiscountPrice()))
                .andExpect(jsonPath("$.discountRate").value(itemDTO.getDiscountRate()));
    }

    @Test
    void updateItem() throws Exception {
        Item savedItem = itemRepository.save(Item.builder()
                .title(itemDTO.getTitle())
                .content(itemDTO.getContent())
                .thumbnail(itemDTO.getThumbnail())
                .descriptionImage(itemDTO.getDescriptionImage())
                .price(itemDTO.getPrice())
                .discountPrice(itemDTO.getDiscountPrice())
                .discountRate(itemDTO.getDiscountRate())
                .sales(itemDTO.getSales())
                .build());

        ItemDTO updatedItemDTO = new ItemDTO();
        updatedItemDTO.setTitle("Updated Title");
        updatedItemDTO.setContent("Updated Content");
        updatedItemDTO.setPrice(2000);
        updatedItemDTO.setDiscountPrice(1800);
        updatedItemDTO.setDiscountRate(20);

        String content = objectMapper.writeValueAsString(updatedItemDTO);

        mockMvc.perform(put("/items/{itemId}", savedItem.getItemId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedItemDTO.getTitle()))
                .andExpect(jsonPath("$.content").value(updatedItemDTO.getContent()))
                .andExpect(jsonPath("$.price").value(updatedItemDTO.getPrice()))
                .andExpect(jsonPath("$.discountPrice").value(updatedItemDTO.getDiscountPrice()))
                .andExpect(jsonPath("$.discountRate").value(updatedItemDTO.getDiscountRate()));
    }

    @Test
    void deleteItem() throws Exception {
        Item savedItem = itemRepository.save(Item.builder()
                .title(itemDTO.getTitle())
                .content(itemDTO.getContent())
                .thumbnail(itemDTO.getThumbnail())
                .descriptionImage(itemDTO.getDescriptionImage())
                .price(itemDTO.getPrice())
                .discountPrice(itemDTO.getDiscountPrice())
                .discountRate(itemDTO.getDiscountRate())
                .sales(itemDTO.getSales())
                .build());

        mockMvc.perform(delete("/items/{itemId}", savedItem.getItemId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/items/{itemId}", savedItem.getItemId()))
                .andExpect(status().isNotFound());
    }
}