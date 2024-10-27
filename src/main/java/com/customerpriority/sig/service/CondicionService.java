package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.Condicion;
import com.customerpriority.sig.repository.CondicionRepository;

@Service
public class CondicionService {

    @Autowired
    private CondicionRepository condicionRepository;

    public List<Condicion> listarTodasLasCondiciones(){
        return condicionRepository.findAll();
    }

}
