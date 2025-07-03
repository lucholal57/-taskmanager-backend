package com.luciano.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luciano.taskmanager.model.User;
import com.luciano.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // Test crear usuario Post
    @Test
    void createUser_returnsCreatedUser() throws Exception {
        // Given
        User user = new User("Luciano", 1L, "luciano@email.com", "admin");
        //  Este es el User que queremos enviar (simula payload JSON)

        // Mock
        when(userService.save(any(User.class))).thenReturn(user);
        //  Mockeamos el servicio: cuando se llama save() devuelve user

        // When
        mockMvc.perform(post("/api/users") // Simula POST a /api/users
                        .contentType(MediaType.APPLICATION_JSON) // Indicamos que enviamos JSON
                        .content(objectMapper.writeValueAsString(user))) // Convertimos el user a JSON
                // Then
                .andExpect(status().isOk()) // Esperamos HTTP 200
                .andExpect(jsonPath("$.name").value("Luciano")) // Verifica campo name
                .andExpect(jsonPath("$.email").value("luciano@email.com")); // Verifica campo email
    }


    // Test obtener usuario por ID GET
    @Test
    void getUSerById_exisntingId_returnsUser() throws Exception {
        // Given
        Long userId = 1L;
        User user = new User("Luciano", userId, "luciano@email.com", "admin");

        // Mock
        when(userService.findById(userId)).thenReturn(user);

        // When
        mockMvc.perform(get("/api/users/{id}", userId))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luciano"))
                .andExpect(jsonPath("$.email").value("luciano@email.com"));
    }

    // Test obtener todos los usuarios GET
    @Test
    void getAllUser_returnsListOfUsers() throws Exception {
        // Given
        List<User> users = Arrays.asList(
                new User("Luciano", 1L, "luciano@email.com", "admin"),
                new User("Mateo", 2L, "mateo@email.com", "user")
        );

        // Mock
        when(userService.findAll()).thenReturn(users);

        // When
        mockMvc.perform(get("/api/users"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andExpect(jsonPath("$[0].name").value("Luciano"))
                .andExpect(jsonPath("$[1].name").value("Mateo"));

    }

    // Test actualizar usuario PUT
    @Test
    void updateUser_exisntinId_returnsUpdateUser() throws Exception {
        // Given
        Long userId = 1L;
        User updatedUser = new User("Mateo", userId, "mateo@email.com", "1234");

        // Mock
        when(userService.findById(userId)).thenReturn(updatedUser);
        when(userService.save(any(User.class))).thenReturn(updatedUser);

        // When
        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mateo"))
                .andExpect(jsonPath("$.email").value("mateo@email.com"));

    }


    // Test eliminar usuario por ID DELETE
    @Test
    void deleteUser_existinId_returnsNoContent() throws Exception{
        // Given
        Long userId = 1L;

        // Mock
        doNothing().when(userService).deleteById(userId);

        // When
        mockMvc.perform(delete("/api/users/{id}",userId))
                // Then
                .andExpect(status().isNoContent());
    }
}
