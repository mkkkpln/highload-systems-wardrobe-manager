package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.UserDto;
import com.example.highloadsystemswardrobemanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получить список пользователей", description = "Возвращает всех пользователей системы")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    })
    @GetMapping
    @Deprecated
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает информацию о пользователе по его уникальному идентификатору."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable @Min(1) Long id) {  // <-- ID >= 1
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает запись пользователя по переданным данным в теле запроса."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в теле запроса")
    })
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) { // <-- @Valid
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userDto));
    }

    @Operation(summary = "Обновить данные пользователя", description = "Изменяет данные пользователя по ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable @Min(1) Long id, // <-- ID >= 1
                                          @Valid @RequestBody UserDto userDto) { // <-- @Valid
        return ResponseEntity.ok(userService.update(id, userDto));
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) { // <-- ID >= 1
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
