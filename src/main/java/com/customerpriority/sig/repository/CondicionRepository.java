package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Condicion;

public interface CondicionRepository extends JpaRepository<Condicion, Integer>{
    
}
