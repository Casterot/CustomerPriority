package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Rol;
import com.customerpriority.sig.repository.RolRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> listarTodosLosRoles(){
        return rolRepository.findAll();
    }

    public Page<Rol> listarRolesPaginados(Pageable pageable){
        return rolRepository.findAll(pageable);
    }

    public Page<Rol> buscarRolesPorKeyword(String keyword, Pageable pageable) {
        return rolRepository.buscarPorRol(keyword, pageable);
    }

    public Rol obtenerRolPorId(int id) {
        return rolRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
    }

    public void guardarRol(Rol rol){
        rolRepository.save(rol);
    }

    public void eliminarRol(int id){
        rolRepository.deleteById(id);
    }

}
