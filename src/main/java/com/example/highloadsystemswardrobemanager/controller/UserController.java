package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.UserDto;
import com.example.highloadsystemswardrobemanager.entity.User;
import com.example.highloadsystemswardrobemanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> users = userService.getAll().stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(userService.getByIdOr404(id)));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        User created = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        User updated = userService.update(id, userDto);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }
}
