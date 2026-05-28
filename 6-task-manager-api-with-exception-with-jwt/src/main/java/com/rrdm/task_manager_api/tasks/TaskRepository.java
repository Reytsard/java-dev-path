package com.rrdm.task_manager_api.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    List<Task> findAllByOwnerId(UUID ownerId);
}
