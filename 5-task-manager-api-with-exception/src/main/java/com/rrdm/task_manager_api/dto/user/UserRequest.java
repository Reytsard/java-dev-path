package com.rrdm.task_manager_api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * This is the request that the users send for creating/updating
 */
public class UserRequest {
    @NotBlank(message = "username is required")
    private String username;

    @Email(message = "Email must be valid")
    @NotBlank(message = "email is required")
    private String email;

    @Size(min = 6, max = 64, message = "Min of 6 and Max of 64 for Passwords")
    @NotBlank(message = "Password is required")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
