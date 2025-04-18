package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Empresa;
import com.customerpriority.sig.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> listarTodasLasEmpresas(){
        return empresaRepository.findAll();
    }

    public Page<Empresa> listarEmpresasPaginadas(Pageable pageable){
        return empresaRepository.findAll(pageable);
    }

    public Page<Empresa> buscarEmpresasPorKeyword(String keyword, Pageable pageable) {
        return empresaRepository.buscarPorEmpresa(keyword, pageable);
    }

    public Empresa obtenerEmpresaPorId(int id) {
        return empresaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
    }

    public void guardarEmpresa(Empresa empresa){
        empresaRepository.save(empresa);
    }

    public void eliminarEmpresa(int id){
        empresaRepository.deleteById(id);
    }

}
