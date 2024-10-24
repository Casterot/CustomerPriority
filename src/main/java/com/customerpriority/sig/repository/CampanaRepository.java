package com.customerpriority.sig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.customerpriority.sig.model.Campana;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampanaRepository extends JpaRepository<Campana, Integer>{
    
    @Query("SELECT c FROM Campana c WHERE " +
           "LOWER(c.segmento) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.nombreCampana) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.gestion) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Campana> buscarPorSegmentoNombreOGestion(@Param("keyword") String keyword, Pageable pageable);
}