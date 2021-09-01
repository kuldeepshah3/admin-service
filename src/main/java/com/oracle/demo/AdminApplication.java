package com.oracle.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.demo.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AdminApplication {

    @Autowired
    private JWTInterceptor jwtInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/health/check").allowedOrigins("*");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(jwtInterceptor)
                        .addPathPatterns("/**/*")
                        .excludePathPatterns("/health/check");
            }
        };
    }
}
