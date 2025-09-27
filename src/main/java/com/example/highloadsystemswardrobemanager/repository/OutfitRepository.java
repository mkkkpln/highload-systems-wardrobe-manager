package com.example.highloadsystemswardrobemanager.repository;

import com.example.highloadsystemswardrobemanager.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    java.util.List<Outfit> findByUser_Id(Long userId);
}
