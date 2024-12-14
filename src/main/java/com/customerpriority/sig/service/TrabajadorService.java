package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.customerpriority.sig.model.Trabajador;
import com.customerpriority.sig.model.Usuario;
import com.customerpriority.sig.repository.TrabajadorRepository;
import com.customerpriority.sig.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Transactional
    public void guardarTrabajador(Trabajador trabajador) {
        boolean esNuevoTrabajador = (trabajador.getIdTrabajador() == 0); // Verificar si es nuevo trabajador
        // Guardar el trabajador primero
        trabajadorRepository.save(trabajador);
        // Crear y guardar el usuario sólo si es un nuevo trabajador
        if (esNuevoTrabajador) {
            Usuario usuario = new Usuario();
            usuario.setUsername(trabajador.getDocumento()); // Usar el DNI como username
            usuario.setPassword(passwordEncoder.encode(trabajador.getDocumento())); // Encriptar el DNI para el password
            usuario.setTrabajador(trabajador); // Asignar el Trabajador
            usuario.setEstado(1); // Establecer el estado (puedes ajustarlo según sea necesario)
            // Guardar el usuario
            usuarioRepository.save(usuario);
        }
    }

    // Método para obtener todos los subordinados (directos e indirectos) de un trabajador
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