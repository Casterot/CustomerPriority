package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Campana;
import com.customerpriority.sig.repository.CampanaRepository;

@Service
public class CampanaService {

    @Autowired
    private CampanaRepository campanaRepository;

    public List<Campana> listarTodasLasCampanas(){
        return campanaRepository.findAll();
    }

}
