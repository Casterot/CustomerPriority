package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.TipoContrato;
import com.customerpriority.sig.repository.TipoContratoRepository;

import jakarta.persistence.EntityNotFoundException;
@Service
public class TipoContratoService {

    @Autowired
    private TipoContratoRepository tipoContratoRepository;

    public List<TipoContrato> listarTodosLosContratos(){
        return tipoContratoRepository.findAll();
    }

    public TipoContrato obtenerContratpPorId(int id) {
    return tipoContratoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contrato no encontrado"));
    }

}