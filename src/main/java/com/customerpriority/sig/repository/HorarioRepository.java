package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Integer>{
    
}
