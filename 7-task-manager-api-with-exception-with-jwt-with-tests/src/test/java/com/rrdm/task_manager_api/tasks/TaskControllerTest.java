package com.rrdm.task_manager_api.tasks;

import com.rrdm.task_manager_api.dto.task.TaskResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
// get(), post(), put(), delete() - builds a fake HTTP request
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
public class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    ObjectMapper objectMapper = new ObjectMapper(); //json converter

    private UUID taskID = UUID.randomUUID();
    private UUID userID = UUID.randomUUID();

    private TaskResponse buildResponse(Task task){
        TaskResponse t = new TaskResponse();
        t.setId(task.getId());
        t.setTitle("Test Task");
        return t;
    }

    /**
     * Get /tasks
     */

    @Test
    @WithMockUser
    public void GET_task_returns200() throws Exception{
        when(taskService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

}
