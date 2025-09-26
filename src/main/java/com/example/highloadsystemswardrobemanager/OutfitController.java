package com.example.highloadsystemswardrobemanager;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/outfits")
public class OutfitController {

    private final OutfitRepository outfitRepository;

    public OutfitController(OutfitRepository outfitRepository) {
        this.outfitRepository = outfitRepository;
    }

    @GetMapping
    public List<Outfit> getAll() {
        return outfitRepository.findAll();
    }

    @PostMapping
    public Outfit create(@RequestBody Outfit outfit) {
        return outfitRepository.save(outfit);
    }

    @GetMapping("/{id}")
    public Outfit getById(@PathVariable Long id) {
        return outfitRepository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Outfit update(@PathVariable Long id, @RequestBody Outfit updated) {
        Outfit outfit = outfitRepository.findById(id).orElseThrow();
        outfit.setTitle(updated.getTitle());
        outfit.setUser(updated.getUser());

        return outfitRepository.save(outfit);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        outfitRepository.deleteById(id);
    }
}

