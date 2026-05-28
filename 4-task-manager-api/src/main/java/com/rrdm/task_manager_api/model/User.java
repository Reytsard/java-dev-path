package com.rrdm.task_manager_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column()
    private String username;

    @Column(length = 64)
    private String password;

    @Column(unique = true)
    private String email;

    public User(){}
    /**
     * mappedby is a column value of the reference table
     * cascade is set for update and delete
     * orphanRemoval is for removing the Entity when it is removed from the table
     * @JsonManagedReference (on User.tasks)
     *
     *   This is the "forward" / parent side. It tells Jackson: "serialize this field normally — include the list of tasks when a
     *    User is serialized."
     */
    @JsonManagedReference //
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
