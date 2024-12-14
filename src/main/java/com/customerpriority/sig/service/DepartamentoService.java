package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.Departamento;
import com.customerpriority.sig.repository.DepartamentoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> listarTodosLosDepartamentos(){
        return departamentoRepository.findAll();
    }

    public Departamento obtenerDepartamentoPorId(int id) {
    return departamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Departamento no encontrado"));
    }

}