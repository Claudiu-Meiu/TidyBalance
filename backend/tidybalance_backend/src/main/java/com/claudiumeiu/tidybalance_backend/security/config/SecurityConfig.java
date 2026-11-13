package com.claudiumeiu.tidybalance_backend.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.spa())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/users/auth/login", "/api/users/account/register")
                                                .permitAll()
                                                .requestMatchers("/api/**").authenticated()
                                                .anyRequest().permitAll())
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(
                                                                (request, response, authException) -> response
                                                                                .sendError(HttpServletResponse.SC_UNAUTHORIZED)))

                                .formLogin(form -> form
                                                .loginProcessingUrl("/api/users/auth/login")
                                                .successHandler((request, response, authentication) -> {
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                })
                                                .failureHandler((request, response, exception) -> {
                                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                                                }))
                                .rememberMe(Customizer.withDefaults())
                                .logout(logout -> logout
                                                .logoutUrl("/api/users/auth/logout")
                                                .deleteCookies("JSESSIONID")
                                                .logoutSuccessHandler((request, response, authentication) -> response
                                                                .setStatus(HttpServletResponse.SC_NO_CONTENT)));

                return http.build();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(12);
        }

}