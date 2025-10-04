package com.example.highloadsystemswardrobemanager.service;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import com.example.highloadsystemswardrobemanager.entity.User;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import com.example.highloadsystemswardrobemanager.mapper.WardrobeItemMapper;
import com.example.highloadsystemswardrobemanager.repository.UserRepository;
import com.example.highloadsystemswardrobemanager.repository.WardrobeItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WardrobeItemService {
    private final WardrobeItemRepository itemRepository;
    private final UserRepository userRepository;
    private final WardrobeItemMapper mapper;

    public WardrobeItemService(WardrobeItemRepository itemRepository,
                               UserRepository userRepository,
                               WardrobeItemMapper mapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<WardrobeItemDto> getAll() {
        return itemRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public WardrobeItemDto getByIdOr404(Long id) {
        return itemRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Item not found: " + id));
    }

    public WardrobeItemDto create(WardrobeItemDto dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Owner not found: " + dto.getOwnerId()));

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

    public Page<WardrobeItemDto> getPagedWithCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 50));
        return itemRepository.findAll(pageable).map(mapper::toDto);
    }

    public List<WardrobeItemDto> getInfiniteScroll(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return itemRepository.findAll(pageable)
                .map(mapper::toDto)
                .toList();
    }
}
