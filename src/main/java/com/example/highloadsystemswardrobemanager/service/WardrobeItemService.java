package com.example.highloadsystemswardrobemanager.service;

import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import com.example.highloadsystemswardrobemanager.repository.WardrobeItemRepository;
import com.example.highloadsystemswardrobemanager.entity.User;
import com.example.highloadsystemswardrobemanager.repository.UserRepository;
import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardrobeItemService {
    private final WardrobeItemRepository itemRepository;
    private final UserRepository userRepository;

    public WardrobeItemService(WardrobeItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<WardrobeItem> getAll() {
        return itemRepository.findAll();
    }

    public WardrobeItem getByIdOr404(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found: " + id));
    }

    public WardrobeItem create(WardrobeItemDto dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Owner not found: " + dto.getOwnerId()));

        WardrobeItem item = new WardrobeItem();
        item.setOwner(owner);
        item.setType(dto.getType());
        item.setBrand(dto.getBrand());
        item.setColor(dto.getColor());
        item.setSeason(dto.getSeason());
        item.setImageUrl(dto.getImageUrl());

        return itemRepository.save(item);
    }

    public WardrobeItem update(Long id, WardrobeItemDto dto) {
        WardrobeItem item = getByIdOr404(id);
        item.setType(dto.getType());
        item.setBrand(dto.getBrand());
        item.setColor(dto.getColor());
        item.setSeason(dto.getSeason());
        item.setImageUrl(dto.getImageUrl());
        return itemRepository.save(item);
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
