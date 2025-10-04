package com.example.highloadsystemswardrobemanager;

import com.example.highloadsystemswardrobemanager.controller.OutfitController;
import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.service.OutfitService;
import com.example.highloadsystemswardrobemanager.service.PagedResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OutfitController.class)
class OutfitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OutfitService outfitService;

    @Test
    void shouldReturnAllOutfits() throws Exception {
        var dto = new OutfitDto();
        dto.setId(1L);
        dto.setTitle("Summer Look");
        dto.setUserId(14L);

        Mockito.when(outfitService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/outfits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Summer Look"))
                .andExpect(jsonPath("$[0].userId").value(14));
    }

    @Test
    void shouldCreateOutfit() throws Exception {
        var dto = new OutfitDto();
        dto.setId(2L);
        dto.setTitle("Autumn Look");
        dto.setUserId(14L);

        Mockito.when(outfitService.create(any(OutfitDto.class))).thenReturn(dto);

        mockMvc.perform(post("/outfits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Autumn Look\",\"userId\":14}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Autumn Look"));
    }

    @Test
    void shouldReturnPagedOutfits() throws Exception {
        var dto = new OutfitDto();
        dto.setId(1L);
        dto.setTitle("Paged Outfit");
        dto.setUserId(1L);

        var result = new PagedResult<>(List.of(dto), 5L);
        Mockito.when(outfitService.getPaged(0, 5)).thenReturn(result);

        mockMvc.perform(get("/outfits/paged?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "5"))
                .andExpect(jsonPath("$[0].title").value("Paged Outfit"));
    }
}
