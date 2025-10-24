package com.example.highloadsystemswardrobemanager.service;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.dto.OutfitItemLinkDto;
import com.example.highloadsystemswardrobemanager.entity.Outfit;
import com.example.highloadsystemswardrobemanager.entity.User;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import com.example.highloadsystemswardrobemanager.mapper.OutfitMapper;
import com.example.highloadsystemswardrobemanager.repository.OutfitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final UserService userService;
    private final WardrobeItemService wardrobeItemService;
    private final OutfitMapper mapper;

//    public List<OutfitDto> getAll() {
//        return outfitRepository.findAll().stream().map(mapper::toDto).toList();
//    }

    public OutfitDto getById(Long id) {
        return outfitRepository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Outfit not found: " + id));
    }

    @Transactional
    public OutfitDto create(OutfitDto dto) {
        User user = userService.getEntityById(dto.getUserId());

        Outfit outfit = mapper.toEntity(dto);
        outfit.setUser(user);

        if (dto.getItems() != null) {
            int idx = 0;
            for (OutfitItemLinkDto link : dto.getItems()) {
                WardrobeItem item = wardrobeItemService.getEntityById(link.getItemId());
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
                WardrobeItem item = wardrobeItemService.getEntityById(link.getItemId());
                outfit.addItem(item, link.getRole(), idx++);
            }
        }

        return mapper.toDto(outfitRepository.save(outfit));
    }

    public PagedResult<OutfitDto> getOutfitsUpTo50(int page, int size) {
        int safePage = atLeastZero(page);
        int safeSize = capTo50(size);
        Page<Outfit> pageData = outfitRepository.findAll(PageRequest.of(safePage, safeSize));

        List<OutfitDto> items = pageData.getContent().stream()
                .map(mapper::toDto)
                .toList();

        return new PagedResult<>(items, pageData.getTotalElements());
    }

    public List<OutfitDto> getInfiniteScroll(int offset, int limit) {
        int safeOffset = atLeastZero(offset);
        int safeLimit  = capTo50(limit);              // ≤ 50
        int pageIndex  = safeOffset / safeLimit;      // приближённая интерпретация offset→page
        return outfitRepository.findAll(PageRequest.of(pageIndex, safeLimit))
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        outfitRepository.deleteById(id);
    }

    // ---------- local helpers ----------
    private static int atLeastZero(int v) { return Math.max(v, 0); }

    private static int capTo50(int v) {
        if (v < 1) return 1;
        return Math.min(v, 50);
    }
}
