package com.customerpriority.sig.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.customerpriority.sig.model.Usuario;
import com.customerpriority.sig.model.Trabajador;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private String nombreCompleto; // Nombre que se mostrará en el navbar
    private Collection<? extends GrantedAuthority> authorities;
    private Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.username = usuario.getUsername();
        this.password = usuario.getPassword();
        
        Trabajador trabajador = usuario.getTrabajador();
        if (trabajador != null) {
            this.nombreCompleto = trabajador.getApellidoPaterno() + " " + trabajador.getApellidoMaterno() + ", " + trabajador.getNombreCompleto();
        } else {
            this.nombreCompleto = "Sin datos";
        }

        this.authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombreRol()))
                .collect(Collectors.toList());

        this.usuario = usuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.getEstado() == 1;
    }
}
