package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.auth.AuthService;
import com.rrdm.task_manager_api.exceptions.UserNotFoundException;
import com.rrdm.task_manager_api.model.PRIORITY;
import com.rrdm.task_manager_api.model.TASKSTATUS;
import com.rrdm.task_manager_api.security.JwtAuthFilter;
import com.rrdm.task_manager_api.security.JwtUtil;
import com.rrdm.task_manager_api.security.SecurityConfig;
import com.rrdm.task_manager_api.users.CustomUserDetailsService;
import com.rrdm.task_manager_api.users.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
// get(), post(), put(), delete() - builds a fake HTTP request
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// status(), jsonPath(), content() - what to check in the response
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// MediaType.APPLICATION_JSON - for setting Content-Type header
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.util.UUID;

@WebMvcTest(TaskController.class)
@Import(SecurityConfig.class)
public class TaskControllerSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    private final UUID taskID = UUID.randomUUID();
    private final UUID userID = UUID.randomUUID();

    public Task buildTask(){
        Task t = new Task();
        t.setId(taskID);
        t.setStatus(TASKSTATUS.PENDING);
        t.setPriority(PRIORITY.HIGH);
        return t;
    }

    @Test
    public void GET_task_returns403_whenNoTokenAndAnonymous() throws Exception {

        //forbidden... isUnauthenticated is for roles that have no access ex. admin vs employee typeshii
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void GET_tasks_id_return403_whenNoTokenAndAnonymous() throws Exception{
        mockMvc.perform(get("/tasks/{id}/",taskID))
                .andExpect(status().isForbidden());
    }

    @Test
    public void POST_tasks_return403_whenNoTokenAndAnonymous() throws Exception{

        Task task = buildTask();

        mockMvc.perform(post("/tasks/{id}/add",taskID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void PUT_task_return401_whenNoTokenAndAnonymous() throws Exception{
        when(taskService.update(buildTask())).thenThrow(new RuntimeException());

        mockMvc.perform(put("/tasks/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildTask())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void GET_tasks_return403_whenNoTokenAndAnonymous() throws Exception{
        mockMvc.perform(get("/tasks/search")
                        .param("keyword","New Title"))
                .andExpect(status().isForbidden());
    }


    @Test
    public void DELETE_task_id_return403_whenNoTokenAndAnonymous() throws Exception{
        mockMvc.perform(delete("/tasks/remove/{id}",taskID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user")
    public void POST_tasks_return404_whenUserNotFound() throws Exception{
        when(taskService.save(eq(taskID),any(Task.class))).thenThrow(
                new UserNotFoundException(userID)
        );

        mockMvc.perform(post("/tasks/{id}/add",taskID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildTask())))
                .andExpect(status().isForbidden());
    }


}
