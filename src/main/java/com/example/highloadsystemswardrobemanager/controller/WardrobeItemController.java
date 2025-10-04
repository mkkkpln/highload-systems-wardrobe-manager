package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.service.WardrobeItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class WardrobeItemController {

    private final WardrobeItemService itemService;

    public WardrobeItemController(WardrobeItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Получить список всех вещей", description = "Возвращает все предметы гардероба пользователя")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public ResponseEntity<List<WardrobeItemDto>> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @Operation(summary = "Получить вещь по ID", description = "Возвращает вещь по её уникальному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вещь найдена"),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WardrobeItemDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getByIdOr404(id));
    }

    @Operation(summary = "Добавить новую вещь", description = "Создает новую вещь в гардеробе пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Вещь успешно добавлена"),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
    })
    @PostMapping
    public ResponseEntity<WardrobeItemDto> create(@RequestBody WardrobeItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(dto));
    }

    @Operation(summary = "Обновить вещь", description = "Обновляет данные существующей вещи по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вещь успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WardrobeItemDto> update(@PathVariable Long id, @RequestBody WardrobeItemDto dto) {
        return ResponseEntity.ok(itemService.update(id, dto));
    }

    @GetMapping("/paged")
    public ResponseEntity<List<WardrobeItemDto>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var items = itemService.getPaged(page, size);
        return ResponseEntity.ok(items);
    }


    @Operation(summary = "Удалить вещь", description = "Удаляет вещь по её ID")
    @ApiResponse(responseCode = "204", description = "Вещь успешно удалена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
