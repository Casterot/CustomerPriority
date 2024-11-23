package com.customerpriority.sig.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.customerpriority.sig.model.Centro;


public interface CentroRepository extends JpaRepository<Centro, Integer>{
    @Query("SELECT c FROM Centro c WHERE " +
           "LOWER(c.nombreCentro) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Centro> buscarPorCentro(@Param("keyword") String keyword, Pageable pageable);
}
