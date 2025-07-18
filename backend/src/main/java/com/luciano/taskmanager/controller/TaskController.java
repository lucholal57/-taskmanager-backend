package com.luciano.taskmanager.controller;

import com.luciano.taskmanager.DTOs.TaskDTO;
import com.luciano.taskmanager.model.Task;
import com.luciano.taskmanager.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    // Inyeccion de servicio
    private final TaskService taskService;

    // Inyeccion del sercicio por constructor
    public TaskController(TaskService taskService){this.taskService = taskService;}

    // Crear Task
    @PostMapping
    public ResponseEntity<Task>createTask(@RequestBody Task task){
        Task nuevo = taskService.save(task);
        return ResponseEntity.ok(nuevo);
    }

    // Obtener todos los Task
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTask() {
        List<TaskDTO> taskDTOs = taskService.findAll().stream()
                .map(TaskDTO::new) // 🔥 convierte cada Task en TaskDTO
                .toList(); // o .collect(Collectors.toList()) en Java 8
        return ResponseEntity.ok(taskDTOs);
    }

    // Obtener Task por id
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        TaskDTO taskDTO = new TaskDTO(task);
        return ResponseEntity.ok(taskDTO);
    }

    // Obtener listado de Task por id de usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getAllTaskByUserId(@PathVariable Long userId){
        List<Task> tasks = taskService.findByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    // Actualizar Task por ID
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updateTask){
        Task existingTask = taskService.findById(id);

        // Actualizar los campos
        existingTask.setTittle(updateTask.getTittle());
        existingTask.setDescription(updateTask.getDescription());
        existingTask.setCompleted(updateTask.getCompleted());
        existingTask.setUser(updateTask.getUser());

        Task saveTask = taskService.save(existingTask);

        return ResponseEntity.ok(saveTask);
    }

    // Eliminar Task po ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
