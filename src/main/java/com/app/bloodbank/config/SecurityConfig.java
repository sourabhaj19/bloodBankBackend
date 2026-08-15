package com.app.bloodbank.config;

import com.app.bloodbank.security.JwtAuthenticationFilter;
import com.app.bloodbank.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * UserDetailsService that loads user by email from UserRepository.
     * Note: use .roles(...) so Spring will automatically prefix ROLE_ for role checks like hasRole("ADMIN").
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByEmail(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(user.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Security filter chain:
     *  - enable CORS support (Spring's CorsFilter will be registered by http.cors())
     *  - disable CSRF for stateless API
     *  - permit OPTIONS preflight requests
     *  - permit auth endpoints and swagger
     *  - require ADMIN role for /api/admin/**
     *  - add JWT filter before UsernamePasswordAuthenticationFilter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthFilter,
                                                   AuthenticationProvider authenticationProvider) throws Exception {
        http
                .cors().and()
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // allow preflight OPTIONS without authentication
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // public endpoints
                        .requestMatchers("/api/users/login", "/api/auth/forgot-password", "/api/users/register",
                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        // admin endpoints (hasRole expects "ADMIN" because we used .roles(...) above)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // everything else must be authenticated
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                // place JWT filter before username/password auth filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS configuration:
     *  - setAllowedOriginPatterns is used for flexible matching (supports wildcards)
     *  - include OPTIONS in allowed methods
     *  - explicitly allow Authorization and other commonly-used headers
     *  - setAllowCredentials(true) if frontend sends credentials (cookies / Authorization with credentials)
     *
     * IMPORTANT:
     *  - If you know the exact origin(s) (e.g. http://localhost:4200, https://example.com), prefer using setAllowedOrigins(List.of("http://localhost:4200"))
     *    instead of allowing patterns for tighter security.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Flexible origin matching. For production prefer explicit origins:
        // config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedOriginPatterns(List.of("*"));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Accept", "Origin"));
        config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        // If your frontend sends cookies or you want credentials (Authorization header is allowed without credentials true,
        // but if browser is sending credentials: setAllowCredentials(true) and DO NOT use "*" for allowedOrigins).
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
