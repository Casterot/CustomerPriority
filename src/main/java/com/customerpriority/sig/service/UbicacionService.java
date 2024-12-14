package com.customerpriority.sig.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Campana;
import com.customerpriority.sig.model.Departamento;
import com.customerpriority.sig.model.Distrito;
import com.customerpriority.sig.model.Provincia;
import com.customerpriority.sig.model.Segmento;
import com.customerpriority.sig.repository.CampanaRepository;
import com.customerpriority.sig.repository.DepartamentoRepository;
import com.customerpriority.sig.repository.DistritoRepository;
import com.customerpriority.sig.repository.ProvinciaRepository;
import com.customerpriority.sig.repository.SegmentoRepository;

import java.util.List;

@Service
public class UbicacionService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private CampanaRepository campanaRepository;

    @Autowired
    private SegmentoRepository segmentoRepository;

    // Obtener todos los departamentos
    public List<Departamento> obtenerDepartamentos() {
        return departamentoRepository.findAll();
    }

    // Obtener las provincias de un departamento específico
    public List<Provincia> obtenerProvinciasPorDepartamento(int idDepartamento) {
        return provinciaRepository.findByDepartamentoIdDepartamento(idDepartamento);
    }

    // Obtener los distritos de una provincia específica
    public List<Distrito> obtenerDistritosPorProvincia(int idProvincia) {
        return distritoRepository.findByProvinciaIdProvincia(idProvincia);
    }


    // Obtener todas las campañas
    public List<Campana> obtenerCampanas() {
        return campanaRepository.findAll();
    }

    // Obtener las provincias de un departamento específico
    public List<Segmento> obtenerSegmentosPorCampana(int idCampana) {
        return segmentoRepository.findByCampanaIdCampana(idCampana);
    }

}