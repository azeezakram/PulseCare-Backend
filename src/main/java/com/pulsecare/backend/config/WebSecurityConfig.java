package com.pulsecare.backend.config;

import com.pulsecare.backend.common.enums.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/user/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/**")
                        .hasAnyRole(
                                Roles.ADMIN.name(), Roles.SUPER_ADMIN.name(),
                                Roles.DOCTOR.name(), Roles.SUPER_DOCTOR.name(),
                                Roles.NURSE.name(), Roles.SUPER_NURSE.name()
                        )

                        .requestMatchers("/api/v1/user/**")
                        .hasAnyRole(
                                Roles.ADMIN.name(), Roles.SUPER_ADMIN.name(),
                                Roles.DOCTOR.name(), Roles.SUPER_DOCTOR.name(),
                                Roles.NURSE.name(), Roles.SUPER_NURSE.name()
                        )


                        .requestMatchers(HttpMethod.POST, "/api/v1/doctor-detail/**")
                        .hasAnyRole(
                                Roles.ADMIN.name(), Roles.SUPER_ADMIN.name(),
                                Roles.DOCTOR.name(), Roles.SUPER_DOCTOR.name()
                        )

                        .requestMatchers(HttpMethod.PUT, "/api/v1/doctor-detail/**")
                        .hasAnyRole(
                                Roles.ADMIN.name(), Roles.SUPER_ADMIN.name(),
                                Roles.DOCTOR.name(), Roles.SUPER_DOCTOR.name()
                        )

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/doctor-detail/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers("/api/v1/doctor-detail/**")
                        .hasAnyRole(
                                Roles.ADMIN.name(), Roles.SUPER_ADMIN.name(),
                                Roles.DOCTOR.name(), Roles.SUPER_DOCTOR.name(),
                                Roles.NURSE.name(), Roles.SUPER_NURSE.name()
                        )

                        .requestMatchers(HttpMethod.POST, "/api/v1/department/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/api/v1/department/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/department/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers("/api/v1/department/**")
                        .hasAnyRole(
                                Roles.ADMIN.name(), Roles.SUPER_ADMIN.name(),
                                Roles.DOCTOR.name(), Roles.SUPER_DOCTOR.name(),
                                Roles.NURSE.name(), Roles.SUPER_NURSE.name()
                        )


                        .requestMatchers(HttpMethod.POST, "/api/v1/specialization/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/api/v1/specialization/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/specialization/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers("/api/v1/specialization/**")
                        .hasAnyRole(
                                Roles.ADMIN.name(), Roles.SUPER_ADMIN.name(),
                                Roles.DOCTOR.name(), Roles.SUPER_DOCTOR.name(),
                                Roles.NURSE.name(), Roles.SUPER_NURSE.name()
                        )


                        .requestMatchers(HttpMethod.POST, "/api/v1/role/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/api/v1/role/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/role/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())

                        .requestMatchers("/api/v1/role/**")
                        .hasAnyRole(
                                Roles.ADMIN.name(), Roles.SUPER_ADMIN.name(),
                                Roles.DOCTOR.name(), Roles.SUPER_DOCTOR.name(),
                                Roles.NURSE.name(), Roles.SUPER_NURSE.name()
                        )


                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration config =
                new org.springframework.web.cors.CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }


}
