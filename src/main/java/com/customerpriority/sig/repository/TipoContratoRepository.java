package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.TipoContrato;

public interface TipoContratoRepository extends JpaRepository<TipoContrato, Integer>{
    
}
