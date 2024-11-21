package com.customerpriority.sig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.customerpriority.sig.model.Trabajador;


public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {

    @Query("SELECT c FROM Trabajador c WHERE " +
           "LOWER(c.documento) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.apellidoPaterno) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.apellidoMaterno) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.nombreCompleto) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Trabajador> buscarPorTrabajadorDniNombre(@Param("keyword") String keyword, Pageable pageable);
}