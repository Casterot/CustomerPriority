package com.customerpriority.sig.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.customerpriority.sig.model.Usuario;
import com.customerpriority.sig.model.Trabajador;

public class CustomUserDetails implements UserDetails {

    private Trabajador trabajador;
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

        // Combinar roles y permisos en las authorities
        HashSet<GrantedAuthority> auths = new HashSet<>();
        
        // Agregar roles
        usuario.getRoles().forEach(rol -> {
            auths.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol()));
            
            // Agregar permisos de cada rol
            rol.getPermisos().forEach(permiso -> 
                auths.add(new SimpleGrantedAuthority(permiso.getNombre()))
            );
        });

        this.authorities = auths;
        this.usuario = usuario;
        this.trabajador = trabajador;
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

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public String getCargoNombre() {
        if (trabajador != null && trabajador.getCargo() != null) {
            return trabajador.getCargo().getNombreCargo();
        }
        return null;
    }
}
