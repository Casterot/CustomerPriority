package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Segmento;
import com.customerpriority.sig.repository.SegmentoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SegmentoService {

    @Autowired
    private SegmentoRepository segmentoRepository;

    public List<Segmento> listarTodosLosSegmentos() {
        return segmentoRepository.findAll();
    }

    public List<Segmento> listarSegmentosPorCampana(int idCampana) {
        return segmentoRepository.findByCampanaIdCampana(idCampana);
    }

    public Page<Segmento> listarSegmentosPaginados(Pageable pageable) {
        return segmentoRepository.findAll(pageable);
    }

    public Page<Segmento> buscarSegmentosPorKeyword(String keyword, Pageable pageable) {
        return segmentoRepository.buscarPorSegmentoNombreOGestion(keyword, pageable);
    }

    public Segmento obtenerSegmentoPorId(int id) {
        return segmentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Segmento no encontrado"));
    }

    public void guardarSegmento(Segmento segmento) {
        segmentoRepository.save(segmento);
    }

    public void eliminarSegmento(int id) {
        segmentoRepository.deleteById(id);
    }

}
