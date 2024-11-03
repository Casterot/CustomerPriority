package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.Genero;
import com.customerpriority.sig.repository.GeneroRepository;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<Genero> listarTodosLosGeneros(){
        return generoRepository.findAll();
    }

}