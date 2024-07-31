package com.retail.store.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class GwApplication {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    public static void main(String[] args) {
        SpringApplication.run(GwApplication.class, args);
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz ->
                authz
                    .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/discounts/**").hasAnyRole(ADMIN, USER)
                    .requestMatchers(HttpMethod.GET, "/**").hasAnyRole(ADMIN, USER)
                    .requestMatchers(HttpMethod.POST, "/**").hasRole(ADMIN)
                    .requestMatchers(HttpMethod.PUT, "/**").hasRole(ADMIN)
                    .requestMatchers(HttpMethod.DELETE, "/**").hasRole(ADMIN)
                    .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User
            .withUsername("user")
            .password(passwordEncoder().encode("user"))
            .roles(USER)
            .build();
        var admin = User
            .withUsername("admin")
            .password(passwordEncoder().encode("admin"))
            .roles(ADMIN)
            .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}