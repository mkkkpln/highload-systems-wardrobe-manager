package com.example.highloadsystemswardrobemanager;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import com.example.highloadsystemswardrobemanager.mapper.WardrobeItemMapper;
import com.example.highloadsystemswardrobemanager.repository.UserRepository;
import com.example.highloadsystemswardrobemanager.repository.WardrobeItemRepository;
import com.example.highloadsystemswardrobemanager.service.WardrobeItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WardrobeItemServiceUnitTest {

    @Mock WardrobeItemRepository itemRepository;
    @Mock UserRepository userRepository;
    @Mock WardrobeItemMapper mapper;

    @InjectMocks WardrobeItemService service;

    @Test
    void getPagedWithCount_sanitizesParams_andMaps() {
        when(itemRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new WardrobeItem())));
        when(mapper.toDto(any(WardrobeItem.class))).thenReturn(new WardrobeItemDto());

        // page < 0 -> 0; size > 50 -> 50
        service.getItemsUpTo50(-3, 1000);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(itemRepository).findAll(captor.capture());
        Pageable p = captor.getValue();

        assertThat(p.getPageNumber()).isEqualTo(0);
        assertThat(p.getPageSize()).isEqualTo(50);
    }

    @Test
    void getInfiniteScroll_capsLimit_andComputesPageIndex() {
        when(itemRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new WardrobeItem())));
        when(mapper.toDto(any(WardrobeItem.class))).thenReturn(new WardrobeItemDto());

        // offset=120, limit=1000 -> limit=50, pageIndex=120/50=2
        service.getInfiniteScroll(120, 1000);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(itemRepository).findAll(captor.capture());
        Pageable p = captor.getValue();

        assertThat(p.getPageNumber()).isEqualTo(2);
        assertThat(p.getPageSize()).isEqualTo(50);
    }

    @Test
    void getInfiniteScroll_limitLessThanOne_becomesOne() {
        when(itemRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        service.getInfiniteScroll(0, 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(itemRepository).findAll(captor.capture());
        Pageable p = captor.getValue();

        assertThat(p.getPageNumber()).isEqualTo(0);
        assertThat(p.getPageSize()).isEqualTo(1);
    }
}
