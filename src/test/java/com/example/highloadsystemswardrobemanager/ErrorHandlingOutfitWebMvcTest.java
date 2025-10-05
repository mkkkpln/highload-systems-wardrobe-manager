package com.example.highloadsystemswardrobemanager;

import com.example.highloadsystemswardrobemanager.controller.OutfitController;
import com.example.highloadsystemswardrobemanager.exception.GlobalExceptionHandler;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import com.example.highloadsystemswardrobemanager.service.OutfitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OutfitController.class)
@Import(GlobalExceptionHandler.class)
class ErrorHandlingOutfitWebMvcTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OutfitService outfitService;

    @Test
    void typeMismatch_returns400() throws Exception {
        mvc.perform(get("/outfits/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("TYPE_MISMATCH"));
    }

    @Test
    void constraintViolation_onMinId_returns400() throws Exception {
        mvc.perform(get("/outfits/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("CONSTRAINT_VIOLATION"));
    }

    @Test
    void bodyValidation_onEmptyDto_returns400() throws Exception {
        mvc.perform(post("/outfits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));
    }

    // --- 404 от GlobalExceptionHandler (NotFoundException) ---
    @Test
    void notFound_returns404() throws Exception {
        when(outfitService.getByIdOr404(123L))
                .thenThrow(new NotFoundException("Outfit not found: 123"));

        mvc.perform(get("/outfits/{id}", 123))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Outfit not found: 123"));
    }

}