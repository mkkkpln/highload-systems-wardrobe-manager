package com.example.highloadsystemswardrobemanager;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class WardrobeItemController {

    private final WardrobeItemRepository itemRepository;

    public WardrobeItemController(WardrobeItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<WardrobeItem> getAll() {
        return itemRepository.findAll();
    }

    @PostMapping
    public WardrobeItem create(@RequestBody WardrobeItem item) {
        return itemRepository.save(item);
    }

    @GetMapping("/{id}")
    public WardrobeItem getById(@PathVariable Long id) {
        return itemRepository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public WardrobeItem update(@PathVariable Long id, @RequestBody WardrobeItem updated) {
        WardrobeItem item = itemRepository.findById(id).orElseThrow();
        item.setBrand(updated.getBrand());
        item.setColor(updated.getColor());
        item.setType(updated.getType());
        item.setSeason(updated.getSeason());
        item.setImageUrl(updated.getImageUrl());
        return itemRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }
}

