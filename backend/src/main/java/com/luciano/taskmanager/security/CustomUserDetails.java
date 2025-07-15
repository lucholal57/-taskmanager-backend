package com.luciano.taskmanager.security;

// Clase que adapta User a UserDetails para Spring Security

import com.luciano.taskmanager.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 📝 Por ahora un solo rol, después podemos soportar múltiples
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 👈 usamos email como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 📝 Podrías manejarlo con un campo en User en el futuro
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 📝 Idem arriba
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // 📝 Podrías tener un campo "active" en User
    }

    // Getter para obtener el User original si lo necesitas
    public User getUser() {
        return user;
    }
}
