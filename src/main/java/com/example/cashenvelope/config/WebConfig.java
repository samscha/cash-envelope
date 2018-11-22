// package com.example.cashenvelope.config;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// @EnableWebMvc
// public class WebConfig implements WebMvcConfigurer {

// @Override
// public void addCorsMappings(CorsRegistry registry) {
// List<String> origins = new ArrayList<String>();

// origins.add(System.getenv("ORIGIN"));

// registry.addMapping("/**").allowedOrigins(origins.toArray(new
// String[origins.size()])).allowCredentials(true);
// }
// }
