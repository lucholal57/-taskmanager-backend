package com.luciano.taskmanager.service;

import com.luciano.taskmanager.exception.UserNotFoundException;
import com.luciano.taskmanager.model.User;
import com.luciano.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // Prepara el usuario para simular un test
        // Given
        User user = new User("Luciano",1L,"luciano@email.com","admin");

        // Busqueda para que lo realice pero no en la db sino desde el objeto creado arriba
        // Mock le dice que devuelva el usuario creado si se pide por el id 1L
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Llama al metodo real del service
        // When
        User result = userService.findById(1L);

        // Verificamos el resultado
        // Then
        assertNotNull(result);
        assertEquals("Luciano", result.getName());
        assertEquals("luciano@email.com", result.getEmail());
    }

    // Test busqueda usuario por id NO OK
    @Test
    void getUserById_notExistingId_throwsException() {
        // Mock
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(1L);
        });
    }

    // Test busqueda de usuario por email OK
    @Test
    void getUserByEmail_existingEmail_returnsUser(){
        // Given
        User user =  new User("Luciano",1L,"luciano@email.com","admin");

        // Mock
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When
        User result =  userService.findByEmail(user.getEmail());

        // Then
        assertNotNull(result);
        assertEquals("Luciano",result.getName());
    }

    // Test busqueda de usuario por email NO OK
    @Test
    void getUserByEmail_notExistingEmail_throwsException(){
        // Mock
        when(userRepository.findByEmail("noexiste@email.com")).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("noexiste@email.com"));
    }

    // Test guarda usuario
    @Test
    void saveUser_validUser_returnsSavedUser() {
        // Given
        User user = new User("Mateo",null,"mateo@email.com","1234");

        // Mock
        when(userRepository.save(user)).thenReturn(user);

        // When
        User result = userService.save(user);

        // Then
        assertNotNull(result);
        assertEquals("Mateo",result.getName());
        assertEquals("mateo@email.com",result.getEmail());
    }

    // Test listar usuarios
    @Test
    void getAllUsers_retunrslistOfUsers(){
        // Given
        List<User> users = List.of(
                new User("Luciano",1L,"luciano@email.com","admin"),
                new User("Agustin",2L,"agustin@email.com","1234")
        );

        // Mock
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals("Luciano",result.get(0).getName());
        assertEquals("Agustin",result.get(1).getName());
    }

    // Test para actualizar usuario OK
    @Test
    void updateUser_existingId_updatesAndReturnsUser(){
        // Given
        Long userId = 1L;
        User existingUser = new User("Luciano", 1L, "luciano@email.com", "admin");

        User updateUser = new User("Mateo", 1L, "mateo@email.com", "1234");

        // Mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(userRepository.save(updateUser)).thenReturn(updateUser);

        // When
        User result = userService.update(updateUser);

        // Then
        assertNotNull(result);
        assertEquals("Mateo", result.getName());
        assertEquals("mateo@email.com", result.getEmail());
    }

    // Test eliminar usuario
    @Test
    void deleteUser_existingId_callsRepositoryDeleteById(){
        // Given
        Long userId =  1L;

        // Mock le agregamos esto por que en el servicio esta validando que el id exista para eliminar por ende tiene que ser true.
        when(userRepository.existsById(userId)).thenReturn(true);

        // When
        userService.deleteById(userId);

        // Then
        verify(userRepository).deleteById(userId);
    }


}
