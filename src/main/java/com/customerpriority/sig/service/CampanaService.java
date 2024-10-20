package com.customerpriority.sig.service;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Campana;
import com.customerpriority.sig.repository.CampanaRepository;

@Service
public class CampanaService {

    @Autowired
    private CampanaRepository campanaRepository;

    public List<Campana> listarTodasLasCampanas(){
        List<Campana> campana = campanaRepository.findAll();
        System.out.println(campana);
        return campana;
    }

    public Page<Campana> listarCampanasPaginadas(int page, int size){
        return campanaRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Campana> buscarCampanaPorId(int id){
        return campanaRepository.findById(id);
    }

    public void guardar(Campana campana){
        campanaRepository.save(campana);
    }

    public void eliminar(int id){
        campanaRepository.deleteById(id);
    }

}
