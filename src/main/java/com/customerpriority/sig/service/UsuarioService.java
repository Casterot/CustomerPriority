package com.customerpriority.sig.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Rol;
import com.customerpriority.sig.model.Usuario;
import com.customerpriority.sig.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
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
        // Si el usuario ya existe, mantener la contraseña original
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario());
        if (usuarioExistente.isPresent()) {
            Usuario usuarioActual = usuarioExistente.get();
            // Mantener la contraseña original
            usuario.setPassword(usuarioActual.getPassword());
        } else {
            // Para usuarios nuevos, encriptar la contraseña si no está ya encriptada
            if (!usuario.getPassword().startsWith("{bcrypt}")) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
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

    public Page<Usuario> listarUsuariosPaginados(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Page<Usuario> buscarUsuariosPorKeyword(String keyword, Pageable pageable) {
        return usuarioRepository.buscarPorKeyword(keyword, pageable);
    }

    public void restablecerContrasenaADNI(int idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Establecer la contraseña como el DNI (username)
            usuario.setPassword(passwordEncoder.encode(usuario.getUsername()));
            usuarioRepository.save(usuario);
        }
    }

    public List<Usuario> buscarUsuariosParaExportar(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return usuarioRepository.buscarPorKeywordParaExportar(keyword);
        }
        return usuarioRepository.findAll();
    }

    public List<Usuario> buscarUsuariosPorKeyword(String keyword) {
        return usuarioRepository.findByUsernameContainingIgnoreCase(keyword);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}