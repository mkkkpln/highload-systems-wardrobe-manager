package com.example.highloadsystemswardrobemanager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WardrobeItemRepository extends JpaRepository<WardrobeItem, Long> {
    // например, поиск всех вещей по пользователю
    java.util.List<WardrobeItem> findByOwnerId(Long ownerId);
}
