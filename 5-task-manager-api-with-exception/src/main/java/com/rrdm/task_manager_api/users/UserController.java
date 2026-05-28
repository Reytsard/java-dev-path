package com.rrdm.task_manager_api.users;

import com.rrdm.task_manager_api.dto.user.UserRequest;
import com.rrdm.task_manager_api.dto.user.UserResponse;
import com.rrdm.task_manager_api.dto.user.UserUpdateRequest;
import com.rrdm.task_manager_api.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable("id")UUID id){
        return userService.findById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponse> save(@Valid @RequestBody UserRequest user){
        return ResponseEntity.ok(userService.save(user));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,@Valid @RequestBody UserUpdateRequest request){
        return ResponseEntity.ok(userService.update(id,request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") UUID id){
        return userService.delete(id);
    }

}
