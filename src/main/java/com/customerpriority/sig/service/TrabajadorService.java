package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Trabajador;
import com.customerpriority.sig.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public List<Trabajador> listarTodosLosTrabajadores() {
        return trabajadorRepository.findAll();
    }

    public Page<Trabajador> listarTrabajadoresPaginados(Pageable pageable) {
        return trabajadorRepository.findAll(pageable);
    }

    public Page<Trabajador> buscarTrabajadoresPorKeyword(String keyword, Pageable pageable) {
        return trabajadorRepository.buscarPorTrabajadorDniNombre(keyword, pageable);
    }

    public Trabajador obtenerTrabajadorPorId(int id) {
        return trabajadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado"));
    }

    public void guardarTrabajador(Trabajador trabajador) {
        if (trabajador.getCorreo() != null && !validarCorreo(trabajador.getCorreo())) {
            throw new IllegalArgumentException("Correo electrónico inválido");
        }

        if (trabajador.getJefeDirecto() != null
                && trabajador.getJefeDirecto().getIdTrabajador() == trabajador.getIdTrabajador()) {
            throw new IllegalArgumentException("Un trabajador no puede ser su propio jefe directo");
        }
        trabajadorRepository.save(trabajador);
    }

    // Método para obtener todos los subordinados (directos e indirectos) de un
    // trabajador
    public List<Trabajador> obtenerTodosLosSubordinados(Trabajador trabajador) {
        List<Trabajador> subordinados = new ArrayList<>();
        obtenerSubordinadosRecursivamente(trabajador, subordinados);
        return subordinados;
    }

    // Método recursivo para obtener subordinados
    private void obtenerSubordinadosRecursivamente(Trabajador trabajador, List<Trabajador> subordinados) {
        List<Trabajador> directos = trabajador.getSubordinados();

        // Validar que directos no sea null
        if (directos == null) {
            directos = new ArrayList<>();
        }

        // Iterar sobre los subordinados directos
        for (Trabajador sub : directos) {
            subordinados.add(sub); // Agregar al resultado
            obtenerSubordinadosRecursivamente(sub, subordinados); // Llamada recursiva
        }
    }

    // Método para validar el correo electrónico
    public boolean validarCorreo(String correo) {
        String emailPattern = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return correo.matches(emailPattern);
    }

    public void eliminarTrabajador(int id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado"));
        if (!trabajador.getSubordinados().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el trabajador porque tiene subordinados");
        }
        // Verificar otras dependencias si es necesario
        try {
            trabajadorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "No se puede eliminar el trabajador porque está asociado a otros datos.");
        }
    }

}