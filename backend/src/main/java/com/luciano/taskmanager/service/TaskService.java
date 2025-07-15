package com.luciano.taskmanager.service;

import com.luciano.taskmanager.model.Task;

import java.util.List;

public interface TaskService {

    Task save(Task task);

    Task update(Task task);

    Task findById(Long id);

    List<Task> findAll();

    List<Task> findByUserId(Long userId);

    void deleteById(Long id);

}
