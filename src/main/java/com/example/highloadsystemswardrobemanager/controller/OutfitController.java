package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.service.OutfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outfits")
public class OutfitController {

    private final OutfitService outfitService;

    public OutfitController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }

    @Operation(summary = "Получить список всех образов", description = "Возвращает все образы, созданные пользователями")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public ResponseEntity<List<OutfitDto>> getAll() {
        return ResponseEntity.ok(outfitService.getAll());
    }

    @Operation(summary = "Получить образ по ID", description = "Возвращает образ с указанным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Образ найден"),
            @ApiResponse(responseCode = "404", description = "Образ не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OutfitDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(outfitService.getByIdOr404(id));
    }

    @Operation(summary = "Создать новый образ", description = "Создает новый образ на основе переданных данных")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Образ успешно создан"),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
    })
    @PostMapping
    public ResponseEntity<OutfitDto> create(@RequestBody OutfitDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(outfitService.create(dto));
    }

    @Operation(summary = "Обновить существующий образ", description = "Обновляет данные образа по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Образ успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Образ не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OutfitDto> update(@PathVariable Long id, @RequestBody OutfitDto dto) {
        return ResponseEntity.ok(outfitService.update(id, dto));
    }

    @Operation(summary = "Удалить образ", description = "Удаляет образ по указанному ID")
    @ApiResponse(responseCode = "204", description = "Образ успешно удален")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        outfitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
