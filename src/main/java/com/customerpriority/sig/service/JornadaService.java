package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.Jornada;
import com.customerpriority.sig.repository.JornadaRepository;
@Service
public class JornadaService {

    @Autowired
    private JornadaRepository jornadaRepository;

    public List<Jornada> listarTodasLasJornadas(){
        return jornadaRepository.findAll();
    }

}