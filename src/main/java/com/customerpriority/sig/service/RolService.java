package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Rol;
import com.customerpriority.sig.model.Permiso;
import com.customerpriority.sig.repository.RolRepository;
import com.customerpriority.sig.repository.PermisoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    public List<Rol> listarTodosLosRoles() {
        return rolRepository.findAll();
    }

    public Page<Rol> listarRolesPaginados(Pageable pageable) {
        return rolRepository.findAll(pageable);
    }

    public Page<Rol> buscarRolesPorKeyword(String keyword, Pageable pageable) {
        return rolRepository.buscarPorRol(keyword, pageable);
    }

    public Rol obtenerRolPorId(int id) {
        return rolRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
    }

    public void guardarRol(Rol rol) {
        rolRepository.save(rol);
    }

    public void eliminarRol(int id) {
        rolRepository.deleteById(id);
    }

    /**
     * Verifica si ya existe un rol con el nombre dado, opcionalmente excluyendo un ID específico.
     * @param nombreRol El nombre del rol a verificar.
     * @param idExcluir El ID del rol a excluir de la búsqueda (usado al editar). Puede ser null o 0 si es un rol nuevo.
     * @return true si ya existe otro rol con ese nombre, false en caso contrario.
     */
    public boolean existeRolConNombre(String nombreRol, Integer idExcluir) {
        Optional<Rol> rolExistente;
        if (idExcluir == null || idExcluir == 0) {
            // Nuevo rol: buscar cualquier rol con ese nombre
            rolExistente = rolRepository.findByNombreRolIgnoreCase(nombreRol);
        } else {
            // Editando rol: buscar cualquier otro rol con ese nombre
            rolExistente = rolRepository.findByNombreRolIgnoreCaseAndIdRolNot(nombreRol, idExcluir);
        }
        return rolExistente.isPresent();
    }

    // Métodos para gestión de permisos
    public void asignarPermiso(int rolId, Long permisoId) {
        Rol rol = obtenerRolPorId(rolId);
        Permiso permiso = permisoRepository.findById(permisoId)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado"));
        rol.agregarPermiso(permiso);
        rolRepository.save(rol);
    }

    public void removerPermiso(int rolId, Long permisoId) {
        Rol rol = obtenerRolPorId(rolId);
        Permiso permiso = permisoRepository.findById(permisoId)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado"));
        rol.removerPermiso(permiso);
        rolRepository.save(rol);
    }

    public Set<Permiso> obtenerPermisos(int rolId) {
        Rol rol = obtenerRolPorId(rolId);
        return rol.getPermisos();
    }
}
