package com.rrdm.task_manager_api.users;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rrdm.task_manager_api.tasks.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "username is required")
    @Column()
    private String username;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 64, message = "Password needs to be a minimum of 6 characters and max of 64 characters")
    @Column(length = 64)
    private String password;

    @Email(message = "Email must be valid")
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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
