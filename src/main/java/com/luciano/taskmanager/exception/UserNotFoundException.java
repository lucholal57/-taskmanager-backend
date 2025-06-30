package com.luciano.taskmanager.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id){
        super("Usuario no encontrado con ID: " + id);
    }

    public UserNotFoundException(String email){
        super("Usuario no encontrado con Email: " + email);
    }
}
