package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.dto.task.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TaskResponse>> findAllById(@PathVariable("id")UUID id){
        return ResponseEntity.ok(taskService.findAllByOwnerId(id));
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<Task> save(@PathVariable("id")UUID id,@Valid @RequestBody Task task){
        return ResponseEntity.status(201).body(taskService.save(id, task));
    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@Valid @RequestBody Task task) {
        Task updated = taskService.update(task);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> search(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(taskService.search(keyword));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Boolean> remove(@PathVariable("id") UUID id){
        return ResponseEntity.status(201).body(taskService.delete(id));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TaskResponse> findByTaskId(@PathVariable("id") UUID id){
        return ResponseEntity.ok(taskService.findByTaskId(id));
    }
}
