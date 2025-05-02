package com.customerpriority.sig.repository;

import com.customerpriority.sig.model.Escuela;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscuelaRepository extends JpaRepository<Escuela, Integer> {
    List<Escuela> findByNombreEscuelaContainingIgnoreCase(String nombreEscuela);
}
