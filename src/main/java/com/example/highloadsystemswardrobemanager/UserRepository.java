package com.example.highloadsystemswardrobemanager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Можно добавлять свои методы, например поиск по email
    User findByEmail(String email);
}
