package com.customerpriority.sig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.customerpriority.sig.model.Segmento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SegmentoRepository extends JpaRepository<Segmento, Integer>{
    
    @Query("SELECT c FROM Segmento c WHERE " +
           "LOWER(c.nombreSegmento) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Segmento> buscarPorSegmentoNombreOGestion(@Param("keyword") String keyword, Pageable pageable);
}