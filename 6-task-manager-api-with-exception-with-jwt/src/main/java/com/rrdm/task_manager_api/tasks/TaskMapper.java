package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.dto.task.TaskRequest;
import com.rrdm.task_manager_api.dto.task.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setOwnerId(task.getOwner().getId());
        response.setOwnerName(task.getOwner().getUsername());
        return response;
    }

    public Task toEntity(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        return task;
    }
}
