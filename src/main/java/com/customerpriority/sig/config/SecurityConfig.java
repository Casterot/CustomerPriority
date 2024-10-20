package com.customerpriority.sig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.customerpriority.sig.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()  // Permitir acceso sin autenticación
                .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
            )
            .formLogin(form -> form
                .loginPage("/login") // Página de inicio de sesión personalizada
                .defaultSuccessUrl("/index", true)  // Página después del inicio de sesión exitoso
                .failureUrl("/login?error=true") // URL para manejar errores
                .permitAll()
            )
            .logout(logout -> logout

                .logoutUrl("/logout") // Configurar la URL de cierre de sesión
                .logoutSuccessUrl("/login?logout=true") // Redirigir después de cerrar sesión
                .invalidateHttpSession(true)
                .clearAuthentication(true) // Invalidar la sesión del usuario
                .deleteCookies("JSESSIONID") // Borrar las cookies de sesión
                .permitAll()
            );
            

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Devuelve el AuthenticationManager configurado
        return authenticationConfiguration.getAuthenticationManager();
    }

}