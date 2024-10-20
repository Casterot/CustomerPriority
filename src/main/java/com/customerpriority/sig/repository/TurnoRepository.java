package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Turno;

public interface TurnoRepository extends JpaRepository<Turno, Integer>{
    
}
