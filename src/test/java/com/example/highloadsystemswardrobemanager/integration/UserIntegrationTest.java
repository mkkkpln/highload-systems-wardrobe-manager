package com.example.highloadsystemswardrobemanager.integration;

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
        UserDto dto = new UserDto(null, "integration@example.com", "IntegrationUser");

        UserDto created = userService.create(dto);
        assertNotNull(created.id());

        UserDto found = userService.getById(created.id());
        assertEquals("IntegrationUser", found.name());
    }
}
