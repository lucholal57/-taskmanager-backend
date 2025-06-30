package com.luciano.taskmanager.service;


import com.luciano.taskmanager.model.User;

import java.util.List;


public interface UserService {


    User save(User user); // Guardar nuevo usuario

    User findById(Long id); // Buscar por ID

    User findByEmail(String email); // Buscar por email

    List<User> findAll(); // Obtener todos

    void deleteById(Long id); // Eliminar por ID

}
