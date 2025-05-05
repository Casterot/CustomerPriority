package com.customerpriority.sig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/usuarios/**").hasAnyAuthority("ADMIN_USUARIOS", "VER_USUARIOS", "EDITAR_USUARIOS")
                .requestMatchers("/roles/**").hasAnyAuthority("ADMIN_ROLES", "VER_ROLES", "EDITAR_ROLES")
                .requestMatchers("/permisos/**").hasAnyAuthority("ADMIN_PERMISOS", "VER_PERMISOS", "EDITAR_PERMISOS")
                .requestMatchers("/trabajadores/**").hasAnyAuthority("ADMIN_TRABAJADORES", "VER_TRABAJADORES", "EDITAR_TRABAJADORES")
                .requestMatchers("/escuelas/**").hasAnyAuthority("ADMIN_ESCUELAS", "VER_ESCUELAS", "EDITAR_ESCUELAS")
                .requestMatchers("/segmentos/**").hasAnyAuthority("ADMIN_SEGMENTOS", "VER_SEGMENTOS", "EDITAR_SEGMENTOS")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/index", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            );

        return http.build();
    }

    @Bean
    AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}