package com.luciano.taskmanager.service;

import com.luciano.taskmanager.exception.TaskNotFoundException;
import com.luciano.taskmanager.model.Task;
import com.luciano.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){this.taskRepository = taskRepository;}


    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task) {
        taskRepository.findById(task.getId())
                .orElseThrow(() -> new TaskNotFoundException(task.getId()));
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        // validar antes de borrar
        if (!taskRepository.existsById(id)){
            throw  new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

}
