package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Trabajador;
import com.customerpriority.sig.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public List<Trabajador> listarTodosLosTrabajadores(){
        return trabajadorRepository.findAll();
    }

    public Page<Trabajador> listarTrabajadoresPaginados(Pageable pageable){
        return trabajadorRepository.findAll(pageable);
    }

    public Page<Trabajador> buscarTrabajadoresPorKeyword(String keyword, Pageable pageable) {
        return trabajadorRepository.buscarPorTrabajadorDniNombre(keyword, pageable);
    }

    public Trabajador obtenerTrabajadorPorId(int id) {
        return trabajadorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado"));
    }

    public void guardarTrabajador(Trabajador trabajador){
        trabajadorRepository.save(trabajador);
    }

    public void eliminarTrabajador(int id){
        trabajadorRepository.deleteById(id);
    }

}
