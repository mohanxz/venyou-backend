
package com.venyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("POST", "/api/bookings/check-availability").permitAll()
                .requestMatchers("GET", "/api/halls", "/api/halls/{id}").permitAll()
                .requestMatchers("GET", "/api/categories", "/api/categories/{name}").permitAll()
                .requestMatchers("GET", "/api/brands", "/api/brands/{id}", "/api/brands/name/{name}").permitAll()
                // Admin endpoints
                .requestMatchers("POST", "/api/halls").hasAuthority("ROLE_ADMIN")
                .requestMatchers("PUT", "/api/halls/{id}").hasAuthority("ROLE_ADMIN")
                .requestMatchers("DELETE", "/api/halls/{id}").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/halls/owner/{ownerId}").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/categories/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/brands/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/owners/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/users/**").hasAuthority("ROLE_ADMIN")
                // User and Admin endpoints
                .requestMatchers("GET", "/api/bookings/process-due-payments").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/bookings/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                // Placeholder controllers
                .requestMatchers("/api/cancellations/**", "/api/support/**", 
                                "/api/availability/**", "/api/pricing/**", "/api/invoices/**", 
                                "/api/notifications/**", "/api/refunds/**", "/api/reviews/**", 
                                "/api/transactions/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                // Deny all other unauthenticated requests
                .anyRequest().denyAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:6060"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
