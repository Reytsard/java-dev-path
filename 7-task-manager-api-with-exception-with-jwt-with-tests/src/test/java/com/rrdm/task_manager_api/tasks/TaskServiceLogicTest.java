package com.rrdm.task_manager_api.tasks;


import com.rrdm.task_manager_api.dto.task.TaskResponse;
import com.rrdm.task_manager_api.exceptions.TaskNotFoundException;
import com.rrdm.task_manager_api.exceptions.UserNotFoundException;
import com.rrdm.task_manager_api.model.PRIORITY;
import com.rrdm.task_manager_api.model.TASKSTATUS;
import com.rrdm.task_manager_api.users.User;
import com.rrdm.task_manager_api.users.UserRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceLogicTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TaskMapper taskMapper;

    @InjectMocks private TaskService taskService;

    private final UUID taskID = UUID.randomUUID();
    private final UUID userID = UUID.randomUUID();

    private User buildUser(){
        User u = new User();
        u.setId(userID);
        return u;
    }

    private Task buildTask(){
        Task t = new Task();
        t.setId(taskID);
        t.setTitle("Test task");
        t.setDescription("Some description");
        t.setStatus(TASKSTATUS.PENDING);
        t.setPriority(PRIORITY.MED);
        return t;
    }

    private TaskResponse buildTaskResponse(Task task){
        TaskResponse r = new TaskResponse();
        r.setId(task.getId());
        r.setTitle(task.getTitle());
        return r;
    }

//    ============================
//    findAll()
//    ============================

    @Test
    public void findAll_returnsMappedTaskResponses(){
        Task task = buildTask();
        TaskResponse response = buildTaskResponse(task);

        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);

        List<TaskResponse> result = taskService.findAll();

        assertEquals(1, result.size());
        assertEquals(taskID, result.getFirst().getId());
    }

    @Test
    public void findAll_returnsEmptyList_whenNoTasksExists(){
        when(taskRepository.findAll()).thenReturn(List.of());

        List<TaskResponse> result = taskService.findAll();

        assertTrue(result.isEmpty());
    }

//    ==============
//    save()
//    ==============


    @Test
    public void save_assignsOwnerAndSavesTask(){
        Task task = buildTask();
        User user = buildUser();

        task.setOwner(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskService.save(user.getId(),task)).thenReturn(task);

        Task result = taskService.save(user.getId(),task);

        assertEquals(result.getOwner(), user);
        verify(taskRepository).save(task);
    }

    @Test
    public void save_throwsUserNotFoundException_whenUserNotFound(){
        when(userRepository.findById(userID)).thenReturn(Optional.empty());

        assertThrows(
                        UserNotFoundException.class,
                        () -> taskService.save(userID, buildTask())
                    );

        verify(userRepository, never()).save(any());
    }


    /**
     * update
     */

    @Test
    public void update_updatesOnlyNonNullFields(){
        Task existing = buildTask();
        existing.setDescription("Old Desc");
        existing.setPriority(PRIORITY.LOW);

        Task incoming = new Task();
        incoming.setId(taskID);
        incoming.setDescription("New Description");
        incoming.setPriority(null);
        incoming.setStatus(null);

        when(taskRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(taskService.save(userID,existing)).thenReturn(existing);

        Task result = taskService.update(incoming);

        assertEquals("New Description", result.getDescription());
        assertEquals(PRIORITY.LOW, result.getPriority());
    }

    @Test
    public void update_updatesStatus_whenProvided(){
        Task existing = buildTask();
        existing.setStatus(TASKSTATUS.PENDING);

        Task incoming = new Task();
        incoming.setId(taskID);
        incoming.setStatus(TASKSTATUS.DONE);

        when(taskRepository.findById(taskID)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task result = taskService.update(incoming);

        assertEquals(TASKSTATUS.DONE, result.getStatus());

    }

    @Test
    public void update_throwsTaskNotFoundException_whenTaskNotExists(){
        Task task = buildTask();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty() );

        assertThrows(
                    TaskNotFoundException.class,
                    () -> taskService.update(task)
                );
    }

    /**
     * search
     */

    @Test
    public void search_mappedResults_whenMatchFound(){
        Task task = buildTask();
        task.setTitle("login");
        TaskResponse taskResponse = buildTaskResponse(task);

        when(
                taskRepository
                .findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("login","login")
            )
                .thenReturn(List.of(task));

        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        List<TaskResponse> results = taskService.search("login");

        assertFalse(results.isEmpty());
        assertEquals(1,results.size());
    }

    @Test
    public void search_returnEmptyList_whenNoMatchFound(){
        when(taskRepository
                .findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("login","login"))
                .thenReturn(List.of());

        List<TaskResponse> result = taskService.search("login");
    }
}
