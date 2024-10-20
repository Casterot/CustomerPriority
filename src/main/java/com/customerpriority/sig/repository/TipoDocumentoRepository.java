package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.TipoDocumento;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer>{
    
}
