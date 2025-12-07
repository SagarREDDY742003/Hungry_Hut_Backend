package com.sagar.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration // This class contains configuration for the application
@EnableWebSecurity // This annotation enables Spring Security’s web security support and integrates it into the Spring application.
public class AppConfig {

    @Bean // Indicates that a method produces a bean to be managed by the Spring container.
    SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        // This method configures the security filter chain, which defines how HTTP requests are secured.

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Uses stateless sessions, meaning no session is stored on the server. This is typical for APIs using JWT (JSON Web Tokens).
                .authorizeHttpRequests(Authorize -> Authorize
                        .requestMatchers("/api/admin/**").hasAnyRole("RESTAURANT_OWNER","ADMIN") //  "/api/admin/**" Only accessible to users with roles RESTAURANT_OWNER or ADMIN.
                        .requestMatchers("/api/**").authenticated() //  "/api/**" Requires authentication.
                        .anyRequest().permitAll()//  All other requests: Allowed without authentication.
                )
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable()) //Disables CSRF protection, which is common for stateless APIs.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    // This method defines how Cross-Origin Resource Sharing (CORS) is handled.
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();

                // Only allows requests from these two domains.
                cfg.setAllowedOrigins(Arrays.asList(
                        "https://hungry-hut-frontend-pi.vercel.app",
                        "http://localhost:3000"
                ));

                // Allows all HTTP methods and headers.
                cfg.setAllowedMethods(Collections.singletonList("*"));
                cfg.setAllowedHeaders(Collections.singletonList("*"));

                //Allows cookies and authorization headers in cross-origin requests.
                cfg.setAllowCredentials(true);

                // Makes the Authorization header visible to the client.
                cfg.setExposedHeaders(Arrays.asList("Authorization"));

                //Caches the CORS response for 1 hour.
                cfg.setMaxAge(3600L);
                return cfg;
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        // Provides a BCrypt-based password encoder, which is a secure way to hash passwords.
        return new BCryptPasswordEncoder();
    }
}
