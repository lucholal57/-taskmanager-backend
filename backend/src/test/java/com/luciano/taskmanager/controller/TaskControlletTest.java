package com.luciano.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luciano.taskmanager.model.Role;
import com.luciano.taskmanager.model.Task;
import com.luciano.taskmanager.model.User;
import com.luciano.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 Desactiva filtros de seguridad
public class TaskControlletTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test crear tarea POST
    @Test
    void createTask_returnsCreatedTask() throws Exception {

        // Given
        // Crear usuario
        User user = new User(1L, "Luciano", "luciano@email.com", "admin", Role.ROLE_ADMIN);
        // Crear Task
        Task task = new Task(null, "Aprender Spring Boot", false, "Practicar tests", user,null);


        // Mock
        when(taskService.save(any(Task.class))).thenReturn(task);

        // When
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(task)))
                // Then
                .andExpect(status().isOk()).andExpect(jsonPath("$.tittle").value("Aprender Spring Boot")).andExpect(jsonPath("$.description").value("Practicar tests")).andExpect(jsonPath("$.completed").value(false));

    }

    // Test obtener tarea por ID GET
    @Test
    void getTaskById_existingId_reutunrstask() throws Exception {
        // Given
        Long taskId = 1L;
        User user = new User(1L, "Luciano", "luciano@email.com", "admin", Role.ROLE_ADMIN);
        Task task = new Task(taskId, "Leer documentación", false, "Leer sobre MockMvc", user,null);

        // Mock
        when(taskService.findById(taskId)).thenReturn(task);

        // When
        mockMvc.perform(get("/api/tasks/{id}", taskId))
                // Then
                .andExpect(status().isOk()).andExpect(jsonPath("$.tittle").value("Leer documentación")).andExpect(jsonPath("$.description").value("Leer sobre MockMvc")).andExpect(jsonPath("$.completed").value(false));


    }

    // Test obtener todas las tareas GET
    @Test
    void getAllTasks_returnslistOfTasks() throws Exception {

        // Given
        User user = new User(1L, "Luciano", "luciano@email.com", "admin", Role.ROLE_ADMIN);
        List<Task> tasks = Arrays.asList(new Task(1L, "Tarea 1", false, "Descripción 1", user,null), new Task(2L, "Tarea 2", true, "Descripción 2", user,null));

        // Mock
        when(taskService.findAll()).thenReturn(tasks);

        // When
        mockMvc.perform(get("/api/tasks"))
                // then
                .andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(tasks.size())).andExpect(jsonPath("$[0].tittle").value("Tarea 1")).andExpect(jsonPath("$[1].tittle").value("Tarea 2"));
    }

    //  Test actualizar tarea PUT
    @Test
    void updateTask_existingId_returnsUpdatedTask() throws Exception {

        // Given
        Long taskId = 1L;
        User user = new User(1L, "Luciano", "luciano@email.com", "admin", Role.ROLE_ADMIN);
        Task updatedTask = new Task(taskId, "Tarea actualizada", true, "Nueva descripción", user,null);

        // Mock
        when(taskService.findById(taskId)).thenReturn(updatedTask);
        when(taskService.save(any(Task.class))).thenReturn(updatedTask);

        // When
        mockMvc.perform(put("/api/tasks/{id}", taskId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedTask)))
                // Then
                .andExpect(status().isOk()).andExpect(jsonPath("$.tittle").value("Tarea actualizada")).andExpect(jsonPath("$.description").value("Nueva descripción")).andExpect(jsonPath("$.completed").value(true));
    }

    // ✅ Test eliminar tarea DELETE
    @Test
    void deleteTask_existingId_returnsNoContent() throws Exception {
        Long taskId = 1L;

        doNothing().when(taskService).deleteById(taskId);

        mockMvc.perform(delete("/api/tasks/{id}", taskId)).andExpect(status().isNoContent());
    }
}
