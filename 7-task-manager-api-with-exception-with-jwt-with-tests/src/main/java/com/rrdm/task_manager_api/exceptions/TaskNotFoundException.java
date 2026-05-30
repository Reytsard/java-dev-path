package com.rrdm.task_manager_api.exceptions;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("Task not found with id: "+id);
    }
}
