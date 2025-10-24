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
        UserDto user = new UserDto(null, "integration_user@example.com", "Integration User");
        UserDto savedUser = userService.create(user);
        assertNotNull(savedUser.id());

        // 2. Создать тестовую вещь
        WardrobeItemDto item = new WardrobeItemDto(
                null,
                ItemType.T_SHIRT,
                "Zara",
                "White",
                Season.SUMMER,
                "https://example.com/tshirt.jpg",
                savedUser.id()
        );
        WardrobeItemDto savedItem = wardrobeItemService.create(item);
        assertNotNull(savedItem.id());

        // 3. Создать тестовый образ с этой вещью
        OutfitItemLinkDto link = new OutfitItemLinkDto(savedItem.id(), OutfitRole.TOP);

        OutfitDto outfit = new OutfitDto(null, "Summer Look", savedUser.id(), List.of(link));

        OutfitDto created = outfitService.create(outfit);

        // 4. Проверить, что всё сохранилось
        assertNotNull(created.id());
        assertEquals("Summer Look", created.title());
        assertEquals(savedUser.id(), created.userId());
        assertEquals(1, created.items().size());
    }

    /**
     * Проверяет обновление существующего образа.
     */
    @Test
    void shouldUpdateOutfitTitle() {
        // Создать пользователя
        UserDto user = new UserDto(null, "update_user@example.com", "Update Tester");
        UserDto savedUser = userService.create(user);

        // Создать пустой образ
        OutfitDto outfit = new OutfitDto(null, "Old Title", savedUser.id(), null);
        OutfitDto created = outfitService.create(outfit);

        // Обновить название
        OutfitDto updatedOutfit = new OutfitDto(created.id(), "New Title", created.userId(), created.items());
        OutfitDto updated = outfitService.update(created.id(), updatedOutfit);

        // Проверка
        assertEquals("New Title", updated.title());
    }

    /**
     * Проверяет удаление образа.
     */
    @Test
    void shouldDeleteOutfit() {
        // Создать пользователя
        UserDto user = new UserDto(null, "delete_user@example.com", "Delete Tester");
        UserDto savedUser = userService.create(user);

        // Создать образ
        OutfitDto outfit = new OutfitDto(null, "To Delete", savedUser.id(), null);
        OutfitDto created = outfitService.create(outfit);
        Long id = created.id();
        assertNotNull(id);

        // Удалить
        outfitService.delete(id);

        // Проверить, что не существует
        assertThrows(Exception.class, () -> outfitService.getById(id));
    }
}
