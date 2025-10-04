package com.example.highloadsystemswardrobemanager;

import com.example.highloadsystemswardrobemanager.dto.UserDto;
import com.example.highloadsystemswardrobemanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class UserIntegrationTest extends BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("wardrobe_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateAndRetrieveUser() {
        UserDto dto = new UserDto();
        dto.setName("IntegrationUser");
        dto.setEmail("integration@example.com");

        UserDto created = userService.create(dto);
        assertNotNull(created.getId());

        UserDto found = userService.getByIdOr404(created.getId());
        assertEquals("IntegrationUser", found.getName());
    }
}
