package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.service.WardrobeItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;   // <— добавили
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@Validated
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

    // пагинация с total count
    @Operation(summary = "Получить вещи с пагинацией", description = "Возвращает список вещей постранично и добавляет X-Total-Count в заголовок ответа")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Страница успешно получена"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры пагинации")
    })
    @GetMapping("/paged")
    public ResponseEntity<List<WardrobeItemDto>> getPagedWithCount(
            @RequestParam(defaultValue = "0") @Min(0) int page,              // <— page >= 0
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {  // <— size 1..50
        var pageResult = itemService.getPagedWithCount(page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(pageResult.getTotalElements()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(pageResult.getContent());
    }

    // бесконечная прокрутка
    @Operation(summary = "Получить вещи (бесконечная прокрутка)", description = "Возвращает следующую часть списка без общего количества записей")
    @ApiResponse(responseCode = "200", description = "Часть списка успешно получена")
    @GetMapping("/scroll")
    public ResponseEntity<List<WardrobeItemDto>> getInfiniteScroll(
            @RequestParam(defaultValue = "0") @Min(0) int offset,            // <— offset >= 0
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int limit) { // <— limit 1..50
        var items = itemService.getInfiniteScroll(offset, limit);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Получить вещь по ID", description = "Возвращает вещь по её уникальному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вещь найдена"),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WardrobeItemDto> getById(@PathVariable @Min(1) Long id) { // <— id >= 1
        return ResponseEntity.ok(itemService.getByIdOr404(id));
    }

    @Operation(summary = "Добавить новую вещь", description = "Создает новую вещь в гардеробе пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Вещь успешно добавлена"),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
    })
    @PostMapping
    public ResponseEntity<WardrobeItemDto> create(@Valid @RequestBody WardrobeItemDto dto) { // <— @Valid
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(dto));
    }

    @Operation(summary = "Обновить вещь", description = "Обновляет данные существующей вещи по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вещь успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Вещь не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WardrobeItemDto> update(@PathVariable @Min(1) Long id,            // <— id >= 1
                                                  @Valid @RequestBody WardrobeItemDto dto) { // <— @Valid
        return ResponseEntity.ok(itemService.update(id, dto));
    }

    @Operation(summary = "Удалить вещь", description = "Удаляет вещь по её ID")
    @ApiResponse(responseCode = "204", description = "Вещь успешно удалена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) { // <— id >= 1
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
