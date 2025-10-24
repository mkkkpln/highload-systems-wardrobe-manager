package com.example.highloadsystemswardrobemanager.integration;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.dto.OutfitItemLinkDto;
import com.example.highloadsystemswardrobemanager.entity.enums.OutfitRole;
import com.example.highloadsystemswardrobemanager.service.OutfitService;
import com.example.highloadsystemswardrobemanager.service.UserService;
import com.example.highloadsystemswardrobemanager.dto.UserDto;
import com.example.highloadsystemswardrobemanager.service.WardrobeItemService;
import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.enums.ItemType;
import com.example.highloadsystemswardrobemanager.entity.enums.Season;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для OutfitService с реальным взаимодействием через Testcontainers.
 * Проверяет создание, чтение и обновление образа с привязанными элементами.
 */
@SpringBootTest
@Testcontainers
class OutfitIntegrationTest extends BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("wardrobe_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private OutfitService outfitService;

    @Autowired
    private UserService userService;

    @Autowired
    private WardrobeItemService wardrobeItemService;

    /**
     * Создаёт пользователя, вещь и образ — затем проверяет корректность связей.
     */
    @Test
    void shouldCreateOutfitWithItemsSuccessfully() {
        // 1. Создать тестового пользователя
        UserDto user = new UserDto();
        user.setEmail("integration_user@example.com");
        user.setName("Integration User");
        UserDto savedUser = userService.create(user);
        assertNotNull(savedUser.getId());

        // 2. Создать тестовую вещь
        WardrobeItemDto item = new WardrobeItemDto();
        item.setType(ItemType.T_SHIRT);
        item.setBrand("Zara");
        item.setColor("White");
        item.setSeason(Season.SUMMER);
        item.setImageUrl("https://example.com/tshirt.jpg");
        item.setOwnerId(savedUser.getId());
        WardrobeItemDto savedItem = wardrobeItemService.create(item);
        assertNotNull(savedItem.getId());

        // 3. Создать тестовый образ с этой вещью
        OutfitItemLinkDto link = new OutfitItemLinkDto();
        link.setItemId(savedItem.getId());
        link.setRole(OutfitRole.TOP);

        OutfitDto outfit = new OutfitDto();
        outfit.setTitle("Summer Look");
        outfit.setUserId(savedUser.getId());
        outfit.setItems(List.of(link));

        OutfitDto created = outfitService.create(outfit);

        // 4. Проверить, что всё сохранилось
        assertNotNull(created.getId());
        assertEquals("Summer Look", created.getTitle());
        assertEquals(savedUser.getId(), created.getUserId());
        assertEquals(1, created.getItems().size());
    }

    /**
     * Проверяет обновление существующего образа.
     */
    @Test
    void shouldUpdateOutfitTitle() {
        // Создать пользователя
        UserDto user = new UserDto();
        user.setEmail("update_user@example.com");
        user.setName("Update Tester");
        UserDto savedUser = userService.create(user);

        // Создать пустой образ
        OutfitDto outfit = new OutfitDto();
        outfit.setTitle("Old Title");
        outfit.setUserId(savedUser.getId());
        OutfitDto created = outfitService.create(outfit);

        // Обновить название
        created.setTitle("New Title");
        OutfitDto updated = outfitService.update(created.getId(), created);

        // Проверка
        assertEquals("New Title", updated.getTitle());
    }

    /**
     * Проверяет удаление образа.
     */
    @Test
    void shouldDeleteOutfit() {
        // Создать пользователя
        UserDto user = new UserDto();
        user.setEmail("delete_user@example.com");
        user.setName("Delete Tester");
        UserDto savedUser = userService.create(user);

        // Создать образ
        OutfitDto outfit = new OutfitDto();
        outfit.setTitle("To Delete");
        outfit.setUserId(savedUser.getId());
        OutfitDto created = outfitService.create(outfit);
        Long id = created.getId();
        assertNotNull(id);

        // Удалить
        outfitService.delete(id);

        // Проверить, что не существует
        assertThrows(Exception.class, () -> outfitService.getByIdOr404(id));
    }
}
