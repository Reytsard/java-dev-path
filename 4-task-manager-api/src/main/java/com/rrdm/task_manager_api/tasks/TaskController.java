package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public List<Task> findAllById(@PathVariable("id")UUID id){
        return taskService.findAllByOwnerId(id);
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<Task> save(@PathVariable("id")UUID id, @RequestBody Task task){
        return taskService.save(id, task);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {
        Task updated = taskService.update(task);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/search")
    public List<Task> search(@RequestParam String keyword) {
        return taskService.search(keyword);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Task> remove(@PathVariable("id") UUID id){
        return taskService.delete(id);
    }
}
