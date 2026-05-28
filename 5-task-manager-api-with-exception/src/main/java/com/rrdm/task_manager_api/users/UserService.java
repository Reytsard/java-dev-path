package com.rrdm.task_manager_api.users;

import com.rrdm.task_manager_api.dto.user.UserRequest;
import com.rrdm.task_manager_api.dto.user.UserResponse;
import com.rrdm.task_manager_api.dto.user.UserUpdateRequest;
import com.rrdm.task_manager_api.exceptions.EmailAlreadyExistsException;
import com.rrdm.task_manager_api.exceptions.UserNotFoundException;
import com.rrdm.task_manager_api.model.User;
import com.rrdm.task_manager_api.model.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse findById(UUID id) {
        User user =  userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse save(UserRequest request) {
        User mappedUser = userMapper.toEntity(request);
        boolean emailExists = this.userRepository.existsUserByEmail(request.getEmail());
        if (emailExists) throw new EmailAlreadyExistsException(request.getEmail());
        User savedUser = userRepository.save(mappedUser);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse update(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        return userMapper.toResponse(userRepository.save(user));
    }

    public ResponseEntity<Boolean> delete(UUID id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
