package com.example.highloadsystemswardrobemanager.controller;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.service.OutfitService;
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

    @GetMapping
    public ResponseEntity<List<OutfitDto>> getAll() {
        return ResponseEntity.ok(outfitService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutfitDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(outfitService.getByIdOr404(id));
    }

    @PostMapping
    public ResponseEntity<OutfitDto> create(@RequestBody OutfitDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(outfitService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OutfitDto> update(@PathVariable Long id, @RequestBody OutfitDto dto) {
        return ResponseEntity.ok(outfitService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        outfitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
