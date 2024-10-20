package com.customerpriority.sig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.customerpriority.sig.model.Trabajador;


public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {
}