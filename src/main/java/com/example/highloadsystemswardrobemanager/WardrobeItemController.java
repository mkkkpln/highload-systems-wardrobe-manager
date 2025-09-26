package com.example.highloadsystemswardrobemanager;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/items")
public class WardrobeItemController {

    private final WardrobeItemRepository itemRepository;

    public WardrobeItemController(WardrobeItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<WardrobeItem> getAllItems() {
        return itemRepository.findAll();
    }

    @PostMapping
    public WardrobeItem createItem(@RequestBody WardrobeItem item) {
        return itemRepository.save(item);
    }

    @GetMapping("/{id}")
    public WardrobeItem getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    @PutMapping("/{id}")
    public WardrobeItem updateItem(@PathVariable Long id, @RequestBody WardrobeItem updated) {
        WardrobeItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        item.setBrand(updated.getBrand());
        item.setColor(updated.getColor());
        item.setType(updated.getType());
        item.setSeason(updated.getSeason());
        item.setImageUrl(updated.getImageUrl());
        item.setOwner(updated.getOwner());

        return itemRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }
}
