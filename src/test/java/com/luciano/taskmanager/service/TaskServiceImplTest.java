package com.luciano.taskmanager.service;

import com.luciano.taskmanager.exception.TaskNotFoundException;
import com.luciano.taskmanager.exception.UserNotFoundException;
import com.luciano.taskmanager.model.Task;
import com.luciano.taskmanager.model.User;
import com.luciano.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskServiceImpl taskService;


    @BeforeEach
    void setup(){
        taskRepository = mock(TaskRepository.class);
        taskService =  new TaskServiceImpl(taskRepository);
    }

    // Test busqueda de Task por ID OK
    @Test
    void getTaskById_existingId_retunrsTask(){
        // Given
        User user = new User("luciano",1L,"luciano@email.com","admin");
        Task task =  new Task(1L,"tarea1",null,"descripcion tarea1",user);

        // Mock
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // When
        Task result = taskService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals("tarea1",result.getTittle());
        assertEquals("descripcion tarea1", result.getDescription());
    }

    // Test busqueda de Task por ID NO OK
    @Test
    void getTaskById_notExistingId_throwsException(){
        // Mock
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(TaskNotFoundException.class,() ->taskService.findById(1L));
    }

    // Test busqueda de Task por ID de usuario OK
    @Test
    void getTaskByUserId_existingUserId_returnsTask(){
        // Given
        User user =  new User("Mateo",2L,"mateo@email.com","1234");
        Task task =  new Task(2L,"tarea2",true,"descripcion tarea2",user);

        // Mock
        when(taskRepository.findByUserId(2L)).thenReturn(List.of(task));

        // When
        List<Task> result = taskService.findByUserId(2L);

        // Then
        assertNotNull(result);
        assertEquals("Mateo",result.get(0).getUser().getName());
        assertEquals("tarea2",result.get(0).getTittle());
        assertEquals("mateo@email.com",result.get(0).getUser().getEmail());
        assertEquals("descripcion tarea2",result.get(0).getDescription());


    }

    // Test busqueda de Task por ID de usuario NO OK
    @Test
    void getTaskByUserId_notExistingId_throwsExcepcion(){
        // Mock
        when(taskRepository.findByUserId(2L)).thenReturn(List.of());

        // When
        List<Task> result = taskService.findByUserId(2L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test guardado de Task OK
    @Test
    void saveTask_validTask_returnsSaveTask(){

        // Given
        User user = new User("luciano",1L,"luciano@email.com","admin");
        Task task =  new Task(1L,"tarea1",null,"descripcion tarea1",user);

        // Mock
        when(taskRepository.save(task)).thenReturn(task);

        // When
        Task  result = taskService.save(task);

        // Then
        assertNotNull(result);
        assertEquals("luciano",result.getUser().getName());
        assertEquals("tarea1",result.getTittle());
        assertEquals("luciano@email.com",result.getUser().getEmail());
        assertEquals("descripcion tarea1",result.getDescription());

    }

    // Test buscar lista Task OK
    @Test
    void getAllTask_returnsListTaks(){
        // Given
        User user =  new User("Mateo",2L,"mateo@email.com","1234");
        List<Task> tasks = List.of(
                new Task(1L, "Tarea1", false, "desc1", null),
                new Task(2L, "Tarea2", true, "desc2", user)
        );

        // Mock
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        List<Task> result =  taskService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // Test buscar lista Task OK
    @Test
    void getAllTask_notExistingList_throwException(){

        // Mock
        when(taskRepository.findAll()).thenReturn(List.of());

        // When
        List<Task> result =  taskService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test para actualizar Task OK
    @Test
    void updateTask_existingId_updatesAndReturnsTask(){
        // Given
        Long taskId = 1L;
        User user = new User("Luciano", 1L, "luciano@email.com", "admin");

        Task existingTask = new Task(taskId, "Tarea vieja", false, "Desc vieja", user);

        Task updatedTask = new Task(taskId, "Tarea nueva", true, "Desc nueva", user);

        // Mock: findById devuelve la existente
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Mock: save devuelve la actualizada
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        // When
        Task result = taskService.update(updatedTask);

        // Then
        assertNotNull(result);
        assertEquals("Tarea nueva", result.getTittle());
        assertEquals("Desc nueva", result.getDescription());
        assertTrue(result.getCompleted());

    }

    // Test eliminar Task OK
    @Test
    void deleteTask_existinId_callsRepositoryDeleteById(){
        //Given
        Long taskId = 1L;

        // Mock le agregamos esto por que en el servicio esta validando que el id exista para eliminar por ende tiene que ser true.
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // When
        taskService.deleteById(taskId);

        // Then
        verify(taskRepository).deleteById(taskId);
    }

}
