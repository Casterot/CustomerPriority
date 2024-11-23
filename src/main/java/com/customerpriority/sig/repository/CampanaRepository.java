package com.customerpriority.sig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.customerpriority.sig.model.Campana;

public interface CampanaRepository extends JpaRepository<Campana, Integer>{
    @Query("SELECT c FROM Campana c WHERE " +
            "LOWER(c.nombreCampana) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Campana> buscarPorCampana(@Param("keyword") String keyword, Pageable pageable);
}
