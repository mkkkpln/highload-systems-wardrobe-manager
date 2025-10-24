package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.controller.OutfitController;
import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.exception.GlobalExceptionHandler;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import com.example.highloadsystemswardrobemanager.service.OutfitService;
import com.example.highloadsystemswardrobemanager.service.PagedResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OutfitController.class)
@Import(GlobalExceptionHandler.class)
class OutfitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OutfitService outfitService;

    // --- CREATE ---
    @Test
    void shouldCreateOutfit() throws Exception {
        var dto = new OutfitDto(2L, "Autumn Look", 14L, null);

        when(outfitService.create(any(OutfitDto.class))).thenReturn(dto);

        mockMvc.perform(post("/outfits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Autumn Look\",\"user_id\":14}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Autumn Look"));
    }

    // --- PAGED ---
    @Test
    void shouldReturnPagedOutfits() throws Exception {
        var dto = new OutfitDto(1L, "Paged Outfit", 1L, null);

        var result = new PagedResult<>(List.of(dto), 5L);
        when(outfitService.getOutfitsUpTo50(0, 5)).thenReturn(result);

        mockMvc.perform(get("/outfits/paged?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "5"))
                .andExpect(jsonPath("$[0].title").value("Paged Outfit"));
    }

    // --- SCROLL (ok) ---
    @Test
    void shouldReturnInfiniteScrollChunk() throws Exception {
        var dto1 = new OutfitDto(1L, "Look #1", 10L, null);
        var dto2 = new OutfitDto(2L, "Look #2", 10L, null);

        when(outfitService.getInfiniteScroll(0, 10)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/outfits/scroll").param("offset", "0").param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Look #1"))
                .andExpect(jsonPath("$[1].title").value("Look #2"));
    }

    // --- SCROLL (limit > 50 -> 400) ---
    @Test
    void scrollShouldRejectTooLargeLimit() throws Exception {
        mockMvc.perform(get("/outfits/scroll").param("offset", "0").param("limit", "1000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("CONSTRAINT_VIOLATION"));
    }

    // --- DELETE (ok) ---
    @Test
    void shouldDeleteOutfit() throws Exception {
        doNothing().when(outfitService).delete(5L);

        mockMvc.perform(delete("/outfits/{id}", 5))
                .andExpect(status().isNoContent());
    }

    // --- DELETE (id=0 -> 400) ---
    @Test
    void deleteShouldRejectZeroId() throws Exception {
        mockMvc.perform(delete("/outfits/{id}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("CONSTRAINT_VIOLATION"));
    }

    // --- GET /{id} type mismatch -> 400 ---
    @Test
    void getByIdTypeMismatch_returns400() throws Exception {
        mockMvc.perform(get("/outfits/{id}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("TYPE_MISMATCH"));
    }

    // --- GET /{id} not found -> 404 ---
    @Test
    void getById_notFound_returns404() throws Exception {
        when(outfitService.getById(123L)).thenThrow(new NotFoundException("Outfit not found: 123"));

        mockMvc.perform(get("/outfits/{id}", 123))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Outfit not found: 123"));
    }

    // --- Paged generic error -> 500 (через GlobalExceptionHandler) ---
    @Test
    void paged_genericException_returns500() throws Exception {
        when(outfitService.getOutfitsUpTo50(0, 10)).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/outfits/paged").param("page", "0").param("size", "10"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}
