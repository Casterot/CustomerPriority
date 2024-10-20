package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Provincia;

public interface ProvinciaRepository extends JpaRepository<Provincia, Integer>{
    
}