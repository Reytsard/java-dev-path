package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.auth.AuthService;
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
    public void GET_task_returns401_whenNoToken() throws Exception {

//        when(taskService.findAll()).thenThrow()
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isUnauthorized());
    }
}
