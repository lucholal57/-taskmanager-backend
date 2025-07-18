package com.luciano.taskmanager.DTOs;

import com.luciano.taskmanager.model.Task;

import java.util.Date;

public class TaskDTO {
    private Long id;
    private String tittle;
    private String description;
    private boolean completed;
    private Long userId;
    private Date createdAt;

    // 🔥 Constructor desde la entidad Task
    public TaskDTO(Task task) {
        this.id = task.getId();
        this.tittle = task.getTittle();
        this.description = task.getDescription();
        this.completed = task.getCompleted();
        this.userId = task.getUser().getId(); // 🔥 saca el id del usuario
        this.createdAt = task.getCreatedAt();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTitle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
