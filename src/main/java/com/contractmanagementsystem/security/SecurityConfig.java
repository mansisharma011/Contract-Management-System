package com.contractmanagementsystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain
    securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http

                // Disable CSRF
                .csrf(csrf ->
                        csrf.disable()
                )

                // Stateless JWT
                .sessionManagement(
                        session ->
                                session
                                        .sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS
                                        )
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/auth/**"
                )
                .permitAll()

                // ADMIN only
                .requestMatchers(
                        "/admin/**"
                )
                .hasRole("ADMIN")

                // ADMIN + CONSULTANT
                .requestMatchers(
                        "/consultant/**"
                )
                .hasRole(
                        "CONSULTANT"
                )

                // CLIENT only
                .requestMatchers(
                        "/client/**"
                )
                .hasRole("CLIENT")

                .anyRequest()
                .authenticated()
        )

                // Add JWT Filter
                .addFilterBefore(
                        jwtAuthenticationFilter,

                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}
