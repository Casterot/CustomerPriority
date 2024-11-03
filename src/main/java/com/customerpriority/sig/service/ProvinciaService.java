package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.Provincia;
import com.customerpriority.sig.repository.ProvinciaRepository;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public List<Provincia> listarTodasLasProvincias(){
        return provinciaRepository.findAll();
    }

}