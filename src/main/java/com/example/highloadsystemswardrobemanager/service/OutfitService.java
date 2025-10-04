package com.example.highloadsystemswardrobemanager.service;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.dto.OutfitItemLinkDto;
import com.example.highloadsystemswardrobemanager.entity.*;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import com.example.highloadsystemswardrobemanager.mapper.OutfitMapper;
import com.example.highloadsystemswardrobemanager.repository.OutfitRepository;
import com.example.highloadsystemswardrobemanager.repository.UserRepository;
import com.example.highloadsystemswardrobemanager.repository.WardrobeItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@Service
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final UserRepository userRepository;
    private final WardrobeItemRepository itemRepository;
    private final OutfitMapper mapper;

    public OutfitService(OutfitRepository outfitRepository,
                         UserRepository userRepository,
                         WardrobeItemRepository itemRepository,
                         OutfitMapper mapper) {
        this.outfitRepository = outfitRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    public List<OutfitDto> getAll() {
        return outfitRepository.findAll().stream().map(mapper::toDto).toList();
    }

    public OutfitDto getByIdOr404(Long id) {
        return outfitRepository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Outfit not found: " + id));
    }

    @Transactional
    public OutfitDto create(OutfitDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + dto.getUserId()));

        Outfit outfit = mapper.toEntity(dto);
        outfit.setUser(user);

        if (dto.getItems() != null) {
            int idx = 0;
            for (OutfitItemLinkDto link : dto.getItems()) {
                WardrobeItem item = itemRepository.findById(link.getItemId())
                        .orElseThrow(() -> new NotFoundException("Item not found: " + link.getItemId()));
                outfit.addItem(item, link.getRole(), idx++);
            }
        }

        return mapper.toDto(outfitRepository.save(outfit));
    }

    @Transactional
    public OutfitDto update(Long id, OutfitDto dto) {
        Outfit outfit = outfitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Outfit not found: " + id));

        outfit.setTitle(dto.getTitle());
        outfit.clearItems();

        if (dto.getItems() != null) {
            int idx = 0;
            for (OutfitItemLinkDto link : dto.getItems()) {
                WardrobeItem item = itemRepository.findById(link.getItemId())
                        .orElseThrow(() -> new NotFoundException("Item not found: " + link.getItemId()));
                outfit.addItem(item, link.getRole(), idx++);
            }
        }

        return mapper.toDto(outfitRepository.save(outfit));
    }

    public PagedResult<OutfitDto> getPaged(int page, int size) {
        var pageable = PageRequest.of(page, Math.min(size, 50));
        Page<Outfit> pageData = outfitRepository.findAll(pageable);

        List<OutfitDto> items = pageData.getContent().stream()
                .map(mapper::toDto)
                .toList();

        return new PagedResult<>(items, pageData.getTotalElements());
    }

    public List<OutfitDto> getInfiniteScroll(int offset, int limit) {
        var pageable = PageRequest.of(offset / limit, limit);
        return outfitRepository.findAll(pageable)
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        outfitRepository.deleteById(id);
    }
}
