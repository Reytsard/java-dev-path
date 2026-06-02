package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.dto.task.TaskResponse;
import com.rrdm.task_manager_api.exceptions.TaskNotFoundException;
import com.rrdm.task_manager_api.model.PRIORITY;
import com.rrdm.task_manager_api.model.TASKSTATUS;
import com.rrdm.task_manager_api.security.JwtAuthFilter;
import com.rrdm.task_manager_api.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
// get(), post(), put(), delete() - builds a fake HTTP request
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// status(), jsonPath(), content() - what to check in the response
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// MediaType.APPLICATION_JSON - for setting Content-Type header
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    JwtAuthFilter jwtAuthFilter;

    ObjectMapper objectMapper = new ObjectMapper(); //json converter

    private final UUID taskID = UUID.randomUUID();
    private final UUID userID = UUID.randomUUID();

    private TaskResponse buildResponse(){
        TaskResponse t = new TaskResponse();
        t.setId(taskID);
        t.setTitle("Test Task");
        return t;
    }

    private Task buildTask(){
        Task task = new Task();
        task.setTitle("New Test");
        task.setPriority(PRIORITY.MED);
        task.setStatus(TASKSTATUS.PENDING);
        return task;
    }

    /**
     * Get /tasks
     */

    @Test
    @WithMockUser
    public void GET_task_returns200_withTaskList() throws Exception{
        TaskResponse t = new TaskResponse();
        t.setId(taskID);
        t.setTitle("Test Task");

        when(taskService.findAll()).thenReturn(List.of(t));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"));

    }

    @Test
    @WithMockUser
    public void GET_task_returns200_withEmptyList() throws Exception{
        when(taskService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

//    should be in another test class
//    @Test
//    public void GET_task_returns401_whenNoToken() throws Exception {
//        mockMvc.perform(get("/tasks"))
//                .andExpect(status().isUnauthorized());
//    }

    /**
     * GET /tasks/{id}
     */
    @Test
    @WithMockUser
    public void GET_tasks_id_returns200_whenFound() throws Exception{
        when(taskService.findAllByOwnerId(userID)).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/tasks/{id}",userID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(taskID.toString()))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    @WithMockUser
    public void GET_tasks_id_return404_whenNotFound() throws Exception{
        when(taskService.findByTaskId(taskID)).thenThrow(new TaskNotFoundException(taskID));

        mockMvc.perform(get("/tasks/id/{id}", taskID))
                .andExpect(status().isNotFound());

    }

//    @Test
//    public void GET_tasks_id_return401_whenNoToken() throws Exception{
//        mockMvc.perform(get("/tasks/{id}/",taskID))
//                .andExpect(status().isUnauthorized());
//    }

    /**
     * /tasks/add/{id}
     */
    @Test
    @WithMockUser
    public void POST_tasks_return201_withCreatedTask() throws Exception{
        Task input = new Task();
        input.setTitle("New Task");
        input.setStatus(TASKSTATUS.PENDING);
        input.setPriority(PRIORITY.MED);

        Task saved = new Task();
        saved.setId(taskID);
        saved.setTitle("New Task");
        input.setStatus(TASKSTATUS.PENDING);
        input.setPriority(PRIORITY.MED);

        String mapped = objectMapper.writeValueAsString(input);

        when(taskService.save(eq(userID), any(Task.class))).thenReturn(saved);
        System.out.println(mapped);

        mockMvc.perform(post("/tasks/{id}/add", userID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))                        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskID.toString()));

    }

//    @Test
//    @WithMockUser
//    public void POST_tasks_return404_whenUserNotFound() throws Exception{
//        when(taskService.save(eq(taskID),any(Task.class))).thenThrow(
//                new UserNotFoundException(userID)
//        );
//
//        mockMvc.perform(post("/tasks/{id}/add",taskID)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new Task())))
//                .andExpect(status().isUnauthorized());
//    }

//    @Test
//    public void POST_tasks_return401_whenNoToken() throws Exception{
//
//        Task task = buildTask();
//
//        mockMvc.perform(post("/tasks/{id}/add",taskID)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(task)))
//                .andExpect(status().isUnauthorized());
//    }

    /**
     * PUT /tasks/update
     */
    @Test
    @WithMockUser
    public void PUT_task_return200_withUpdatedTask() throws Exception{
        Task update = buildTask();
        update.setId(taskID);
        update.setDescription("old desc");

        Task updated = buildTask();
        updated.setId(taskID);
        updated.setDescription("Updated desc");

        when(taskService.update(any(Task.class))).thenReturn(updated);

        mockMvc.perform(put("/tasks/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated desc"));
    }

    @Test
    @WithMockUser
    public void PUT_task_return404_whenTaskNotFound() throws Exception{
        when(taskService.update(any(Task.class))).thenThrow(new TaskNotFoundException(taskID));

        mockMvc.perform(put("/tasks/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(buildTask())))
                .andExpect(status().isNotFound());
    }

    //!!!must be better if testing 401 are in a separate test, because this has no filters, disabled at the start, this class only check outputs...
//    @Test
//    public void PUT_task_return401_whenNoToken() throws Exception{
//        when(taskService.update(buildTask())).thenThrow(new RuntimeException())
//
//        mockMvc.perform(put("/tasks/update")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(buildTask())))
//                .andExpect(status().isUnauthorized());
//    }

    /**
     * GET /tasks/search
     */

    @Test
    @WithMockUser
    public void GET_tasks_search_return200_whenMatchFound() throws Exception{
        TaskResponse response = new TaskResponse();
        response.setId(taskID);
        response.setTitle("New Title");

        when(taskService.search("Title"))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/tasks/search")
                        .param("keyword","Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("New Title"));
    }

    @Test
    @WithMockUser
    public void GET_tasks_return200_whenNoMatchFound() throws Exception{
        when(taskService.search("New Title")).thenReturn(List.of());

        mockMvc.perform(get("/tasks/search")
                    .param("keyword","New Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

//    @Test
//    public void GET_tasks_return401_whenNoToken() throws Exception{
//        mockMvc.perform(get("/tasks/search")
//                        .param("keyword","New Title"))
//                .andExpect(status().isUnauthorized());
//    }

    /**
     * delete
     */

    @Test
    @WithMockUser
    public void DELETE_task_id_return200_whenDeleted() throws Exception{
        when(taskService.delete(taskID)).thenReturn(true);

        mockMvc.perform(delete("/tasks/remove/{id}",taskID))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void DELETE_task_id_return404_whenNotFound() throws Exception{
        when(taskService.delete(taskID)).thenThrow(new TaskNotFoundException(taskID));

        mockMvc.perform(delete("/tasks/remove/{id}",taskID))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void DELETE_task_id_return401_whenNoToken() throws Exception{
//        mockMvc.perform(delete("/tasks/remove/{id}",taskID))
//                .andExpect(status().isUnauthorized());
//    }
}
