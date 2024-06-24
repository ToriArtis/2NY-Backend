package com.mega._NY.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.jwt.SecretKey;
import com.mega._NY.item.controller.ItemController;
import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtToken.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecretKey.class)
})
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private ItemDTO itemDTO;

    @BeforeEach
    void setUp() {
        itemDTO = new ItemDTO();
        itemDTO.setItemId(1L);
        itemDTO.setTitle("Test Item");
        itemDTO.setContent("Test Content");
        itemDTO.setPrice(1000);
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(any(ItemDTO.class))).thenReturn(itemDTO);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemId").value(itemDTO.getItemId()))
                .andExpect(jsonPath("$.title").value(itemDTO.getTitle()))
                .andExpect(jsonPath("$.content").value(itemDTO.getContent()))
                .andExpect(jsonPath("$.price").value(itemDTO.getPrice()));

        verify(itemService, times(1)).createItem(any(ItemDTO.class));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getItem(anyLong())).thenReturn(itemDTO);

        mockMvc.perform(get("/items/{itemId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(itemDTO.getItemId()))
                .andExpect(jsonPath("$.title").value(itemDTO.getTitle()))
                .andExpect(jsonPath("$.content").value(itemDTO.getContent()))
                .andExpect(jsonPath("$.price").value(itemDTO.getPrice()));

        verify(itemService, times(1)).getItem(anyLong());
    }

    @Test
    void getItems() throws Exception {
        List<ItemDTO> itemDTOList = Arrays.asList(itemDTO);
        when(itemService.getItems()).thenReturn(itemDTOList);

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemId").value(itemDTO.getItemId()))
                .andExpect(jsonPath("$[0].title").value(itemDTO.getTitle()))
                .andExpect(jsonPath("$[0].content").value(itemDTO.getContent()))
                .andExpect(jsonPath("$[0].price").value(itemDTO.getPrice()));

        verify(itemService, times(1)).getItems();
    }

    @Test
    void updateItem() throws Exception {
        ItemDTO updatedItemDTO = new ItemDTO();
        updatedItemDTO.setItemId(1L);
        updatedItemDTO.setTitle("Updated Item");
        updatedItemDTO.setContent("Updated Content");
        updatedItemDTO.setPrice(2000);
        when(itemService.updateItem(anyLong(), any(ItemDTO.class))).thenReturn(updatedItemDTO);

        mockMvc.perform(put("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedItemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(updatedItemDTO.getItemId()))
                .andExpect(jsonPath("$.title").value(updatedItemDTO.getTitle()))
                .andExpect(jsonPath("$.content").value(updatedItemDTO.getContent()))
                .andExpect(jsonPath("$.price").value(updatedItemDTO.getPrice()));

        verify(itemService, times(1)).updateItem(anyLong(), any(ItemDTO.class));
    }

    @Test
    void deleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(anyLong());

        mockMvc.perform(delete("/items/{itemId}", 1L))
                .andExpect(status().isNoContent());

        verify(itemService, times(1)).deleteItem(anyLong());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}