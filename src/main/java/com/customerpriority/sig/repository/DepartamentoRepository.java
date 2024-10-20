package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Integer>{
    
}
