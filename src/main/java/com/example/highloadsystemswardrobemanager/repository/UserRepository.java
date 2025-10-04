package com.example.highloadsystemswardrobemanager.repository;

import com.example.highloadsystemswardrobemanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
