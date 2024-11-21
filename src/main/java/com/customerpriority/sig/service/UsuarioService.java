package com.customerpriority.sig.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Rol;
import com.customerpriority.sig.model.Usuario;
import com.customerpriority.sig.repository.RolRepository;
import com.customerpriority.sig.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder; 

    
    public UsuarioService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(int id) {
        return usuarioRepository.findById(id);
    }

    public void save(Usuario usuario) {
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    public void delete(int id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }


    //Encripta las constraseñas que se hayan guardado en la base de datos
    public void updatePasswords() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            if (!usuario.getPassword().startsWith("{bcrypt}")) {
                String encryptedPassword = passwordEncoder.encode(usuario.getPassword());
                usuario.setPassword(encryptedPassword);
                usuarioRepository.save(usuario);
            }
        }
    }

    // Método para asignar un rol a un usuario
    public void asignarRol(Usuario usuario, Rol rol) {
        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
    }

    // Método para eliminar un rol de un usuario
    public void eliminarRol(Usuario usuario, Rol rol) {
        usuario.getRoles().remove(rol);
        usuarioRepository.save(usuario);
    }
    
    // Método para obtener todos los roles de un usuario
    public Set<Rol> obtenerRolesDeUsuario(int usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(Usuario::getRoles)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}