package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import com.example.highloadsystemswardrobemanager.service.WardrobeItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class WardrobeItemController {

    private final WardrobeItemService itemService;

    public WardrobeItemController(WardrobeItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<WardrobeItemDto>> getAll() {
        List<WardrobeItemDto> items = itemService.getAll().stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WardrobeItemDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(itemService.getByIdOr404(id)));
    }

    @PostMapping
    public ResponseEntity<WardrobeItemDto> create(@RequestBody WardrobeItemDto dto) {
        WardrobeItem created = itemService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WardrobeItemDto> update(@PathVariable Long id, @RequestBody WardrobeItemDto dto) {
        WardrobeItem updated = itemService.update(id, dto);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private WardrobeItemDto toDto(WardrobeItem item) {
        WardrobeItemDto dto = new WardrobeItemDto();
        dto.setId(item.getId());
        dto.setType(item.getType());
        dto.setBrand(item.getBrand());
        dto.setColor(item.getColor());
        dto.setSeason(item.getSeason());
        dto.setImageUrl(item.getImageUrl());
        dto.setOwnerId(item.getOwner().getId());
        return dto;
    }
}
