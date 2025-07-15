package com.luciano.taskmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite solicitudes desde cualquier origen (puedes poner localhost:4200 para mayor seguridad)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*") // Acepta todos los headers
                .allowCredentials(true); // Permitir enviar cookies (si usas sesiones o JWT)
    }
}
