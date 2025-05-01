package com.quicklist.quicklist.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")  // Frontend React
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Les méthodes autorisées
                .allowedHeaders("*");  // Autoriser tous les headers
    }
}
