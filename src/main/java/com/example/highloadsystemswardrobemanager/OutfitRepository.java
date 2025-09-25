package com.example.highloadsystemswardrobemanager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {
    java.util.List<Outfit> findByUser_Id(Long userId);
}
