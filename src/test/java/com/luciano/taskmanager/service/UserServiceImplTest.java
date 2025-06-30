package com.luciano.taskmanager.service;

import com.luciano.taskmanager.exception.UserNotFoundException;
import com.luciano.taskmanager.model.User;
import com.luciano.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp(){
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    // Test busqueda usuario por id OK
    @Test
    void getUserById_existingId_returnsUser(){
        // Given
        User user = new User();
        user.setId(1L);
        user.setName("Luciano");
        user.setEmail("luciano@email.com");
        user.setPassword("admin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User result = userService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Luciano", result.getName());
        assertEquals("luciano@email.com", result.getEmail());
    }

    // Test busqueda usuario por id NO OK
    @Test
    void getUserById_notExistingId_throwsException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(99L);
        });
    }

}
