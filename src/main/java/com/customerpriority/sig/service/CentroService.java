package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Centro;
import com.customerpriority.sig.repository.CentroRepository;

import jakarta.persistence.EntityNotFoundException;
@Service
public class CentroService {

    @Autowired
    private CentroRepository centroRepository;

    public List<Centro> listarTodosLosCentros(){
        return centroRepository.findAll();
    }

    public Page<Centro> listarCentrosPaginados(Pageable pageable){
        return centroRepository.findAll(pageable);
    }

    public Page<Centro> buscarCentrosPorKeyword(String keyword, Pageable pageable) {
        return centroRepository.buscarPorCentro(keyword, pageable);
    }

    public Centro obtenerCentroPorId(int id) {
        return centroRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Centro no encontrado"));
    }

    public void guardarCentro(Centro centro){
        centroRepository.save(centro);
    }

    public void eliminarCentro(int id){
        centroRepository.deleteById(id);
    }
}