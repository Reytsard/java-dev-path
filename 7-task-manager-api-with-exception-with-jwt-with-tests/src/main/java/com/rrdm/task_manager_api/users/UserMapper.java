package com.rrdm.task_manager_api.users;

import com.rrdm.task_manager_api.dto.user.UserRequest;
import com.rrdm.task_manager_api.dto.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

    public User toEntity(UserRequest response){
        User user = new User();
        user.setEmail(response.getEmail());
        user.setUsername(response.getUsername());
        return user;
    }
}
