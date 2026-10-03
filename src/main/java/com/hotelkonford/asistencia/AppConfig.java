package com.hotelkonford.asistencia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Habilita el acceso vía web a los archivos subidos en uploads/permisos/
        registry.addResourceHandler("/uploads/permisos/**")
                .addResourceLocations("file:uploads/permisos/");
    }
}