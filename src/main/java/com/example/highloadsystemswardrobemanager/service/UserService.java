package com.example.highloadsystemswardrobemanager.service;

import com.example.highloadsystemswardrobemanager.entity.User;
import com.example.highloadsystemswardrobemanager.repository.UserRepository;
import com.example.highloadsystemswardrobemanager.dto.UserDto;
import com.example.highloadsystemswardrobemanager.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByIdOr404(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    public User create(UserDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        return userRepository.save(user);
    }

    public User update(Long id, UserDto dto) {
        User user = getByIdOr404(id);
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
