package com.customerpriority.sig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.customerpriority.sig.model.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Permiso findByNombre(String nombre);
    Page<Permiso> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}