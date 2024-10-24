package com.customerpriority.sig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.customerpriority.sig.model.Cargo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    @Query("SELECT c FROM Cargo c WHERE " +
            "LOWER(c.nombreCargo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Cargo> buscarPorCargo(@Param("keyword") String keyword, Pageable pageable);
}