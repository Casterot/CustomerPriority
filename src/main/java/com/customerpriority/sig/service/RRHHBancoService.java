package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.RRHHBanco;
import com.customerpriority.sig.repository.RRHHBancoRepository;

@Service
public class RRHHBancoService {

    @Autowired
    private RRHHBancoRepository rrhhBancoRepository;

    public List<RRHHBanco> listarTodasLasJornadas(){
        return rrhhBancoRepository.findAll();
    }

}