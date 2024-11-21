package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.TipoGestion;
import com.customerpriority.sig.repository.TipoGestionRepository;

@Service
public class TipoGestionService {

    @Autowired
    private TipoGestionRepository tipoGestionRepository;

    public List<TipoGestion> listarTodasLasGestiones(){
        return tipoGestionRepository.findAll();
    }

}