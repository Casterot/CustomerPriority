package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.Distrito;
import com.customerpriority.sig.repository.DistritoRepository;

@Service
public class DistritoService {

    @Autowired
    private DistritoRepository distritoRepository;

    public List<Distrito> listarTodosLosDistritos(){
        return distritoRepository.findAll();
    }

}