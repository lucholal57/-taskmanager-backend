package com.luciano.taskmanager.service;

import com.luciano.taskmanager.exception.UserNotFoundException;
import com.luciano.taskmanager.model.User;
import com.luciano.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    // Constructor con @Autowired implícito desde Spring 4.3+
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        // Encriptar password antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow( () -> new UserNotFoundException(email));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    public User update(User user) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        // Validar antes de borrar
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
