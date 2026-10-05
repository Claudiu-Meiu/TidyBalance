package com.claudiumeiu.tidybalance_backend.security.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.claudiumeiu.tidybalance_backend.user.entity.User;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.spa())
                                .cors(Customizer.withDefaults())

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/authenticate").permitAll()
                                                .anyRequest().authenticated())
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(
                                                                (request, response, authException) -> response
                                                                                .sendError(HttpServletResponse.SC_UNAUTHORIZED)))

                                .formLogin(form -> form
                                                .loginProcessingUrl("/authenticate")
                                                .successHandler((request, response, authentication) -> {
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                })
                                                .failureHandler((request, response, exception) -> {
                                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                                                }));

                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(
                                List.of("http://localhost:4200"));

                configuration.setAllowedMethods(
                                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                configuration.setAllowedHeaders(
                                List.of("*"));

                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", configuration);

                return source;
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(12);
        }

        // Only for testing
        @Bean
        CommandLineRunner createUser(
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {

                return args -> {
                        User user = new User();
                        user.setUsername("user");
                        user.setPassword(passwordEncoder.encode("pass"));
                        user.setDisplayedName("John Doe");

                        userRepository.save(user);
                };
        }
}