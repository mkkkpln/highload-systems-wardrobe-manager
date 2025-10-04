package com.example.highloadsystemswardrobemanager;

import com.example.highloadsystemswardrobemanager.controller.UserController;
import com.example.highloadsystemswardrobemanager.dto.UserDto;
import com.example.highloadsystemswardrobemanager.service.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnListOfUsers() throws Exception {
        var dto = new UserDto();
        dto.setId(1L);
        dto.setName("Alice");
        dto.setEmail("alice@example.com");

        Mockito.when(userService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        var dto = new UserDto();
        dto.setId(2L);
        dto.setName("Bob");
        dto.setEmail("bob@example.com");

        Mockito.when(userService.create(any(UserDto.class))).thenReturn(dto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bob\",\"email\":\"bob@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).delete(1L);
    }
}
