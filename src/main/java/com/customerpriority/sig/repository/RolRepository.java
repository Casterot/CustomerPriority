package com.customerpriority.sig.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.customerpriority.sig.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer>{

    @Query("SELECT c FROM Rol c WHERE " +
            "LOWER(c.nombreRol) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Rol> buscarPorRol(@Param("keyword") String keyword, Pageable pageable);

    Optional<Rol> findByNombreRolIgnoreCase(String nombreRol);

    Optional<Rol> findByNombreRolIgnoreCaseAndIdRolNot(String nombreRol, Integer idRol);
}