package com.rrdm.task_manager_api.users;

import com.rrdm.task_manager_api.model.User;
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
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable("id")UUID id){
        return userService.findById(id);
    }

    @PostMapping("/add")
    public User save(@RequestBody User user){
        return userService.save(user);
    }

    @PatchMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user){
        User updated = userService.update(user);
        if(updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") UUID id){
        return userService.delete(id);
    }

}
