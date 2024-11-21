package com.customerpriority.sig.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customerpriority.sig.model.Distrito;

public interface DistritoRepository extends JpaRepository<Distrito, Integer>{
    List<Distrito> findByProvinciaIdProvincia(Integer idProvincia);
}
