package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.TipoGestion;
import com.customerpriority.sig.repository.SegmentoRepository;
import com.customerpriority.sig.repository.TipoGestionRepository;

@Service
public class TipoGestionService {

    @Autowired
    private TipoGestionRepository tipoGestionRepository;

    @Autowired
    private SegmentoRepository segmentoRepository;

    public List<TipoGestion> listarTodasLasGestiones() {
        return tipoGestionRepository.findAll();
    }

    public Optional<TipoGestion> obtenerTipoGestionPorId(int idGestion) {
        return tipoGestionRepository.findById(idGestion);
    }

    public List<TipoGestion> listarGestionesPorSegmento(int idSegmento) {
        return segmentoRepository.findTipoGestionBySegmentoId(idSegmento);
    }

}