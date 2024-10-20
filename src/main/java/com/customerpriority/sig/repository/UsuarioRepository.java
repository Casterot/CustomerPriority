package com.customerpriority.sig.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
    Optional<Usuario> findByUsername(String username);
}
