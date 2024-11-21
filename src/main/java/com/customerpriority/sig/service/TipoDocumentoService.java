package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.TipoDocumento;
import com.customerpriority.sig.repository.TipoDocumentoRepository;
@Service
public class TipoDocumentoService {

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    public List<TipoDocumento> listarTodosLosDocumentos(){
        return tipoDocumentoRepository.findAll();
    }

}