package com.example.highloadsystemswardrobemanager.repository;

import com.example.highloadsystemswardrobemanager.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    java.util.List<Outfit> findByUser_Id(Long userId);

    // для пагинированных списков аутфитов
    Page<Outfit> findAll(Pageable pageable);
}
