package com.example.highloadsystemswardrobemanager.service;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import com.example.highloadsystemswardrobemanager.entity.User;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import com.example.highloadsystemswardrobemanager.mapper.WardrobeItemMapper;
import com.example.highloadsystemswardrobemanager.repository.WardrobeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WardrobeItemService {
    private final WardrobeItemRepository itemRepository;
    private final UserService userService;
    private final WardrobeItemMapper mapper;

//    public List<WardrobeItemDto> getAll() {
//        return itemRepository.findAll()
//                .stream()
//                .map(mapper::toDto)
//                .collect(Collectors.toList());
//    }

    public WardrobeItemDto getById(Long id) {
        return itemRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Item not found: " + id));
    }

    /**
     * Метод для получения WardrobeItem entity (для использования другими сервисами)
     */
    public WardrobeItem getEntityById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found: " + id));
    }

    public WardrobeItemDto create(WardrobeItemDto dto) {
        User owner = userService.getEntityById(dto.getOwnerId());

        WardrobeItem entity = mapper.toEntity(dto);
        entity.setOwner(owner);

        return mapper.toDto(itemRepository.save(entity));
    }

    public WardrobeItemDto update(Long id, WardrobeItemDto dto) {
        WardrobeItem entity = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found: " + id));

        // обновляем через маппер
        mapper.updateEntityFromDto(dto, entity);

        return mapper.toDto(itemRepository.save(entity));
    }

    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Item not found: " + id);
        }
        itemRepository.deleteById(id);
    }

    public Page<WardrobeItemDto> getItemsUpTo50(int page, int size) {
        int safePage = atLeastZero(page);
        int safeSize = capTo50(size);
        Pageable pageable = PageRequest.of(safePage, safeSize);
        return itemRepository.findAll(pageable).map(mapper::toDto);
    }

    public List<WardrobeItemDto> getInfiniteScroll(int offset, int limit) {
        int safeOffset = atLeastZero(offset);
        int safeLimit  = capTo50(limit);            // ≤ 50
        int pageIndex  = safeOffset / safeLimit;    // приближённая интерпретация offset→page
        return itemRepository.findAll(PageRequest.of(pageIndex, safeLimit))
                .map(mapper::toDto)
                .toList();
    }

    // ---------- local helpers ----------
    private static int atLeastZero(int v) { return Math.max(v, 0); }

    private static int capTo50(int v) {
        if (v < 1) return 1;
        return Math.min(v, 50);
    }
}
