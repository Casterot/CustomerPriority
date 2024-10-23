package com.customerpriority.sig.service;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Campana;
import com.customerpriority.sig.repository.CampanaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CampanaService {

    @Autowired
    private CampanaRepository campanaRepository;

    public List<Campana> listarTodasLasCampanas(){
        return campanaRepository.findAll();
    }

    public Page<Campana> listarCampanasPaginadas(Pageable pageable){
        return campanaRepository.findAll(pageable);
    }

    public Campana obtenerCampanaPorId(int id) {
        return campanaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));
    }

    public void guardarCampana(Campana campana){
        campanaRepository.save(campana);
    }

    public void eliminarCampana(int id){
        campanaRepository.deleteById(id);
    }

}
