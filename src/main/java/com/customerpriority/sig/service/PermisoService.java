package com.customerpriority.sig.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.customerpriority.sig.model.Permiso;
import com.customerpriority.sig.repository.PermisoRepository;

@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    public List<Permiso> listarTodos() {
        return permisoRepository.findAll();
    }

    public Page<Permiso> listarTodos(Pageable pageable) {
        return permisoRepository.findAll(pageable);
    }

    public Permiso obtenerPorId(Long id) {
        return permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
    }

    public Permiso guardar(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    public void eliminar(Long id) {
        permisoRepository.deleteById(id);
    }

    public Permiso obtenerPorNombre(String nombre) {
        return permisoRepository.findByNombre(nombre);
    }

    public Page<Permiso> buscarPermisos(String search, Pageable pageable) {
        return permisoRepository.findByNombreContainingIgnoreCase(search, pageable);
    }
}