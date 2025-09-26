package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.entity.Outfit;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import com.example.highloadsystemswardrobemanager.service.OutfitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/outfits")
public class OutfitController {

    private final OutfitService outfitService;

    public OutfitController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }

    @GetMapping
    public ResponseEntity<List<OutfitDto>> getAll() {
        List<OutfitDto> outfits = outfitService.getAll().stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(outfits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutfitDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(outfitService.getByIdOr404(id)));
    }

    @PostMapping
    public ResponseEntity<OutfitDto> create(@RequestBody OutfitDto dto) {
        Outfit created = outfitService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OutfitDto> update(@PathVariable Long id, @RequestBody OutfitDto dto) {
        Outfit updated = outfitService.update(id, dto);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        outfitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private OutfitDto toDto(Outfit outfit) {
        OutfitDto dto = new OutfitDto();
        dto.setId(outfit.getId());
        dto.setTitle(outfit.getTitle());
        dto.setUserId(outfit.getUser().getId());

        dto.setItemIds(outfit.getItems().stream()
                .map(WardrobeItem::getId)
                .collect(Collectors.toList()));

        return dto;
    }
}
