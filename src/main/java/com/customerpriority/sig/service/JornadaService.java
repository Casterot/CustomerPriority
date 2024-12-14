package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Jornada;
import com.customerpriority.sig.repository.JornadaRepository;

import jakarta.persistence.EntityNotFoundException;
@Service
public class JornadaService {

    @Autowired
    private JornadaRepository jornadaRepository;

    public List<Jornada> listarTodasLasJornadas(){
        return jornadaRepository.findAll();
    }

    public Jornada obtenerJornadaPorId(int id) {
        return jornadaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jornada no encontrada"));
    }

}