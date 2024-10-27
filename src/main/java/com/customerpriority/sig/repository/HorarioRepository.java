package com.customerpriority.sig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.customerpriority.sig.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Integer>{
    @Query("SELECT c FROM Horario c WHERE " +
           "LOWER(c.nombreHorario) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Horario> buscarPorHorario(@Param("keyword") String keyword, Pageable pageable);
}
