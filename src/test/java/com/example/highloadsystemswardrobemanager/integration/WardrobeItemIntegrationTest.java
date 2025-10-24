package com.example.highloadsystemswardrobemanager.integration;

import com.example.highloadsystemswardrobemanager.dto.UserDto;
import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.enums.ItemType;
import com.example.highloadsystemswardrobemanager.entity.enums.Season;
import com.example.highloadsystemswardrobemanager.service.UserService;
import com.example.highloadsystemswardrobemanager.service.WardrobeItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class WardrobeItemIntegrationTest extends BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("wardrobe_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private WardrobeItemService wardrobeItemService;

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateItemSuccessfully() {
        // 1. Создаём владельца
        UserDto user = new UserDto(null, "item_owner_" + System.nanoTime() + "@example.com", "Item Owner");
        UserDto savedUser = userService.create(user);
        assertNotNull(savedUser.id());

        // 2. Создаём вещь с реальным ownerId
        WardrobeItemDto dto = new WardrobeItemDto(
                null,
                ItemType.T_SHIRT,
                "Zara",
                "White",
                Season.SUMMER,
                "https://example.com/tshirt.jpg",
                savedUser.id()
        );

        var created = wardrobeItemService.create(dto);

        // 3. Проверяем, что всё корректно
        assertNotNull(created);
        assertEquals("Zara", created.brand());
        assertEquals(ItemType.T_SHIRT, created.type());
        assertEquals(savedUser.id(), created.ownerId());
    }
}
