package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.service.WardrobeItemService;
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

    @GetMapping
    public ResponseEntity<List<WardrobeItemDto>> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WardrobeItemDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getByIdOr404(id));
    }

    @PostMapping
    public ResponseEntity<WardrobeItemDto> create(@RequestBody WardrobeItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WardrobeItemDto> update(@PathVariable Long id, @RequestBody WardrobeItemDto dto) {
        return ResponseEntity.ok(itemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
