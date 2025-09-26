package com.example.highloadsystemswardrobemanager.service;

import com.example.highloadsystemswardrobemanager.entity.Outfit;
import com.example.highloadsystemswardrobemanager.repository.OutfitRepository;
import com.example.highloadsystemswardrobemanager.entity.User;
import com.example.highloadsystemswardrobemanager.repository.UserRepository;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import com.example.highloadsystemswardrobemanager.repository.WardrobeItemRepository;
import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OutfitService {
    private final OutfitRepository outfitRepository;
    private final UserRepository userRepository;
    private final WardrobeItemRepository itemRepository;

    public OutfitService(OutfitRepository outfitRepository, UserRepository userRepository, WardrobeItemRepository itemRepository) {
        this.outfitRepository = outfitRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public List<Outfit> getAll() {
        return outfitRepository.findAll();
    }

    public Outfit getByIdOr404(Long id) {
        return outfitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Outfit not found: " + id));
    }

    @Transactional
    public Outfit create(OutfitDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + dto.getUserId()));

        Outfit outfit = new Outfit();
        outfit.setTitle(dto.getTitle());
        outfit.setUser(user);

        if (dto.getItemIds() != null) {
            for (Long itemId : dto.getItemIds()) {
                WardrobeItem item = itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
                outfit.getItems().add(item);
            }
        }

        return outfitRepository.save(outfit);
    }

    @Transactional
    public Outfit update(Long id, OutfitDto dto) {
        Outfit outfit = getByIdOr404(id);
        outfit.setTitle(dto.getTitle());

        if (dto.getItemIds() != null) {
            outfit.getItems().clear();
            for (Long itemId : dto.getItemIds()) {
                WardrobeItem item = itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
                outfit.getItems().add(item);
            }
        }

        return outfitRepository.save(outfit);
    }

    public void delete(Long id) {
        outfitRepository.deleteById(id);
    }
}
