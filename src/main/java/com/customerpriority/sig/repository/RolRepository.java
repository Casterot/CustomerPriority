package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer>{
    
}