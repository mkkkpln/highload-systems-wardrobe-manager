package com.example.highloadsystemswardrobemanager.repository;

import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WardrobeItemRepository extends JpaRepository<WardrobeItem, Long> {
    // например, поиск всех вещей по пользователю
    java.util.List<WardrobeItem> findByOwnerId(Long ownerId);

    // для пагинации (findAll с ограничением 50 на страницу)
    Page<WardrobeItem> findAll(Pageable pageable);
}
