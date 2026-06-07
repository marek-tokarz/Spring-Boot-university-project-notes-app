package com.lab.notesapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // text to hash: pass12#$

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Definiowanie zapytań do pobierania użytkowników i ról
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT user_id, pw, active FROM members WHERE user_id=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT user_id, role FROM roles WHERE user_id=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/categories").hasAnyRole("USER", "MODERATOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        // Użyj HTTP Basic do autoryzacji
        http.httpBasic(Customizer.withDefaults());

        // Wyłącz CSRF dla REST API
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
