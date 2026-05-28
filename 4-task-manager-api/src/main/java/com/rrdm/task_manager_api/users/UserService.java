package com.rrdm.task_manager_api.users;

import com.rrdm.task_manager_api.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.findById(user.getId()).map(existing -> {
            if (user.getUsername() != null) existing.setUsername(user.getUsername());
            if (user.getPassword() != null) existing.setPassword(user.getPassword());
            if (user.getEmail() != null) existing.setEmail(user.getEmail());
            return userRepository.save(existing);
        }).orElse(null);
    }

    public ResponseEntity<Boolean> delete(UUID id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
