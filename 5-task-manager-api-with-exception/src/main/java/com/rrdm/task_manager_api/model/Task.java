package com.rrdm.task_manager_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Title must not be empty")
    @Column(nullable = false)
    private String title;

    @Column(nullable=true)
    private String description;

    @NotBlank(message = "priority must not be empty")
    @Column()
    @Enumerated(EnumType.STRING)
    private PRIORITY priority;

    @NotBlank(message = "status must not be empty")
    @Column()
    @Enumerated(EnumType.STRING)
    private TASKSTATUS status;

    /**
     * @JsonBackReference (on Task.owner)
     *
     *   This is the "back" / child side. It tells Jackson: "skip this field during serialization — do NOT include the owner when
     *    a Task is serialized."
     *
     *   // GET /tasks → Task is serialized WITHOUT its owner
     *   {
     *     "id": "...",
     *     "title": "Fix bug",
     *     "priority": "HIGH"
     *     // no "owner" field here
     *   }
     *   //doesnt serialized user
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Task() {
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

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

    public PRIORITY getPriority() {
        return priority;
    }

    public void setPriority(PRIORITY priority) {
        this.priority = priority;
    }

    public TASKSTATUS getStatus() {
        return status;
    }

    public void setStatus(TASKSTATUS status) {
        this.status = status;
    }

}
