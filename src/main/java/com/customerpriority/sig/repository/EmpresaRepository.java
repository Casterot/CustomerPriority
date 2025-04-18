package com.customerpriority.sig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.customerpriority.sig.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    
        @Query("SELECT c FROM Empresa c WHERE " +
            "LOWER(c.nombreEmpresa) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Empresa> buscarPorEmpresa(@Param("keyword") String keyword, Pageable pageable);
}