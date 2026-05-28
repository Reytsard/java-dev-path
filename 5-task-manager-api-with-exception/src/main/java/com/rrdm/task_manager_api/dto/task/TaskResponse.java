package com.rrdm.task_manager_api.dto.task;

import com.rrdm.task_manager_api.model.PRIORITY;
import com.rrdm.task_manager_api.model.TASKSTATUS;

import java.util.UUID;

public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private TASKSTATUS status;
    private PRIORITY priority;
    private UUID ownerId;      // just the ID, not the whole User object
    private String ownerName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TASKSTATUS getStatus() {
        return status;
    }

    public void setStatus(TASKSTATUS status) {
        this.status = status;
    }

    public PRIORITY getPriority() {
        return priority;
    }

    public void setPriority(PRIORITY priority) {
        this.priority = priority;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
