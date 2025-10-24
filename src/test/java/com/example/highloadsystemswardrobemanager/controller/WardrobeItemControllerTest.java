package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.enums.ItemType;
import com.example.highloadsystemswardrobemanager.entity.enums.Season;
import com.example.highloadsystemswardrobemanager.service.WardrobeItemService;
import com.example.highloadsystemswardrobemanager.controller.WardrobeItemController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WardrobeItemController.class)
class WardrobeItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WardrobeItemService wardrobeItemService;

    private WardrobeItemDto sampleItem() {
        return new WardrobeItemDto(
                1L,
                ItemType.T_SHIRT,
                "Zara",
                "White",
                Season.SUMMER,
                "https://example.com/image.jpg",
                10L
        );
    }


    @Test
    @DisplayName("GET /items/{id} — вернуть вещь по ID")
    void getById() throws Exception {
        Mockito.when(wardrobeItemService.getById(1L))
                .thenReturn(sampleItem());

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("White"));
    }

    @Test
    @DisplayName("POST /items — создать новую вещь")
    void createItem() throws Exception {
        Mockito.when(wardrobeItemService.create(any()))
                .thenReturn(sampleItem());

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": "T_SHIRT",
                                  "brand": "Zara",
                                  "color": "White",
                                  "season": "SUMMER",
                                  "image_url": "https://example.com/image.jpg",
                                  "owner_id": 10
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Zara"));
    }

    @Test
    @DisplayName("PUT /items/{id} — обновить вещь")
    void updateItem() throws Exception {
        Mockito.when(wardrobeItemService.update(eq(1L), any()))
                .thenReturn(sampleItem());

        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": "T_SHIRT",
                                  "brand": "Uniqlo",
                                  "color": "Beige",
                                  "season": "SUMMER",
                                  "image_url": "https://example.com/beige.jpg",
                                  "owner_id": 10
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("T_SHIRT"));
    }

    @Test
    @DisplayName("DELETE /items/{id} — удалить вещь")
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(wardrobeItemService).delete(1L);
    }

    @Test
    @DisplayName("GET /items/paged — вернуть страницу с заголовком X-Total-Count")
    void getPagedItems() throws Exception {
        var dtoList = List.of(sampleItem());
        var page = new org.springframework.data.domain.PageImpl<>(dtoList);
        Mockito.when(wardrobeItemService.getItemsUpTo50(0, 10)).thenReturn(page);

        mockMvc.perform(get("/items/paged?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "1"));
    }

    @Test
    @DisplayName("GET /items/scroll — вернуть часть списка без total count")
    void getInfiniteScroll() throws Exception {
        Mockito.when(wardrobeItemService.getInfiniteScroll(0, 5))
                .thenReturn(List.of(sampleItem()));

        mockMvc.perform(get("/items/scroll?offset=0&limit=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].owner_id").value(10));
    }
}
