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
        UserDto user = new UserDto();
        user.setEmail("item_owner_" + System.nanoTime() + "@example.com");
        user.setName("Item Owner");
        UserDto savedUser = userService.create(user);
        assertNotNull(savedUser.getId());

        // 2. Создаём вещь с реальным ownerId
        WardrobeItemDto dto = new WardrobeItemDto();
        dto.setType(ItemType.T_SHIRT);
        dto.setBrand("Zara");
        dto.setColor("White");
        dto.setSeason(Season.SUMMER);
        dto.setImageUrl("https://example.com/tshirt.jpg");
        dto.setOwnerId(savedUser.getId());

        var created = wardrobeItemService.create(dto);

        // 3. Проверяем, что всё корректно
        assertNotNull(created);
        assertEquals("Zara", created.getBrand());
        assertEquals(ItemType.T_SHIRT, created.getType());
        assertEquals(savedUser.getId(), created.getOwnerId());
    }
}
