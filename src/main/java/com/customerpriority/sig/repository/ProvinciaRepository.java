package com.customerpriority.sig.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Provincia;

public interface ProvinciaRepository extends JpaRepository<Provincia, Integer>{
    List<Provincia> findByDepartamentoIdDepartamento(Integer idDepartamento);
}