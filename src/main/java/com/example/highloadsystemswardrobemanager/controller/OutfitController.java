package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.service.OutfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outfits")
@Validated
public class OutfitController {

    private final OutfitService outfitService;

    public OutfitController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }

//    @Operation(summary = "Получить список всех образов", description = "Возвращает все образы, созданные пользователями")
//    @ApiResponse(responseCode = "200", description = "Список успешно получен")
//    @GetMapping
//    @Deprecated
//    public ResponseEntity<List<OutfitDto>> getAll() {
//        return ResponseEntity.ok(outfitService.getAll());
//    }

    @Operation(summary = "Получить образ по ID", description = "Возвращает образ с указанным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Образ найден"),
            @ApiResponse(responseCode = "404", description = "Образ не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OutfitDto> getById(@PathVariable @Min(1) Long id) { // <-- ID >= 1
        return ResponseEntity.ok(outfitService.getById(id));
    }

    @Operation(summary = "Создать новый образ", description = "Создает новый образ на основе переданных данных")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Образ успешно создан"),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
    })
    @PostMapping
    public ResponseEntity<OutfitDto> create(@Valid @RequestBody OutfitDto dto) { // <-- @Valid
        return ResponseEntity.status(HttpStatus.CREATED).body(outfitService.create(dto));
    }

    @Operation(summary = "Обновить существующий образ", description = "Обновляет данные образа по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Образ успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Образ не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OutfitDto> update(@PathVariable @Min(1) Long id, // <-- ID >= 1
                                            @Valid @RequestBody OutfitDto dto) { // <-- @Valid
        return ResponseEntity.ok(outfitService.update(id, dto));
    }

    @Operation(summary = "Получить образы с пагинацией",
            description = "Возвращает список образов постранично (не более 50 за запрос). В заголовке ответа указывается общее количество записей.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Образы успешно получены")
    })
    @GetMapping("/paged")
    public ResponseEntity<List<OutfitDto>> getPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,      // <-- page >= 0
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) { // <-- 1..50
        var result = outfitService.getOutfitsUpTo50(page, size);
        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(result.totalCount()))
                .body(result.items());
    }

    @Operation(summary = "Бесконечная прокрутка образов",
            description = "Возвращает часть списка образов без указания общего количества записей (для 'ленты').")
    @GetMapping("/scroll")
    public ResponseEntity<List<OutfitDto>> getInfiniteScroll(
            @RequestParam(defaultValue = "0") @Min(0) int offset,    // <-- offset >= 0
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int limit) { // <-- 1..50
        return ResponseEntity.ok(outfitService.getInfiniteScroll(offset, limit));
    }

    @Operation(summary = "Удалить образ", description = "Удаляет образ по указанному ID")
    @ApiResponse(responseCode = "204", description = "Образ успешно удален")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) { // <-- ID >= 1
        outfitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
