package com.rrdm.task_manager_api.users;

import com.rrdm.task_manager_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
