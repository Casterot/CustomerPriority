package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.Modalidad;
import com.customerpriority.sig.repository.ModalidadRepository;

@Service
public class ModalidadService {

    @Autowired
    private ModalidadRepository modalidadRepository;

    public List<Modalidad> listarTodasLasModalidades(){
        return modalidadRepository.findAll();
    }

}