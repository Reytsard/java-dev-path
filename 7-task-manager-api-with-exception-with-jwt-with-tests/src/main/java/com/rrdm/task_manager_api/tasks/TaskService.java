package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.dto.task.TaskResponse;
import com.rrdm.task_manager_api.exceptions.TaskNotFoundException;
import com.rrdm.task_manager_api.exceptions.UserNotFoundException;
import com.rrdm.task_manager_api.users.User;
import com.rrdm.task_manager_api.users.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskResponse> findAll() {
        return taskRepository
                .findAll()
                .stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Task save(UUID id, Task task) {
        User owner = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        task.setOwner(owner);
        return this.taskRepository.save(task);
    }

    public Task update(Task task) {
        return taskRepository.findById(task.getId()).map(existing -> {
            if(task.getDescription() != null) existing.setDescription(task.getDescription());
            if(task.getPriority() != null) existing.setPriority(task.getPriority());
            if(task.getStatus() != null) existing.setStatus(task.getStatus());
            return taskRepository.save(existing);
        }).orElseThrow(() -> new TaskNotFoundException(task.getId()));
    }

    public List<TaskResponse> search(String string){
        return taskRepository
                .findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(string,string)
                .stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public boolean delete(UUID id) {
        Optional<Task> taskToDelete = taskRepository.findById(id);
        if(taskToDelete.isEmpty()) return false;
        taskRepository.delete(taskToDelete.get());
        return true;
    }

    public List<TaskResponse> findAllByOwnerId(UUID id) {
        return taskRepository
                .findAllByOwnerId(id)
                .stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse findByTaskId(UUID id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponse(task);
    }
}
