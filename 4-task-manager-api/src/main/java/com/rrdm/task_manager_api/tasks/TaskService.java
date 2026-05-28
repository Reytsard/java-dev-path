package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.model.Task;
import com.rrdm.task_manager_api.model.User;
import com.rrdm.task_manager_api.users.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public ResponseEntity<Task> save(UUID id, Task task) {
        Optional<User> owner = this.userRepository.findById(id);
        if(owner.isEmpty()) ResponseEntity.notFound().build();
        task.setOwner(owner.get());
        return ResponseEntity.ok(taskRepository.save(task));
    }

    public Task update(Task task) {
        return taskRepository.findById(task.getId()).map(existing -> {
            if(task.getDescription() != null) existing.setDescription(task.getDescription());
            if(task.getPriority() != null) existing.setPriority(task.getPriority());
            if(task.getStatus() != null) existing.setStatus(task.getStatus());
            if(task.getDescription() != null) existing.setDescription(task.getDescription());
            return taskRepository.save(existing);
        }).orElse(null);
    }

    public List<Task> search(String string){
        return taskRepository.findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(string,string);
    }

    public ResponseEntity<Task> delete(UUID id) {
        Optional<Task> taskToDelete = taskRepository.findById(id);
        if(taskToDelete.isEmpty()) return ResponseEntity.notFound().build();
        taskRepository.delete(taskToDelete.get());
        return ResponseEntity.ok().build();
    }

    public List<Task> findAllByOwnerId(UUID id) {
        return taskRepository.findAllByOwnerId(id);
    }
}
