package com.customerpriority.sig.service;

import com.customerpriority.sig.model.Escuela;
import com.customerpriority.sig.model.Trabajador;
import com.customerpriority.sig.repository.EscuelaRepository;
import com.customerpriority.sig.repository.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EscuelaService {
    @Autowired
    private EscuelaRepository escuelaRepository;
    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public List<Escuela> listarTodas() {
        return escuelaRepository.findAll();
    }

    public Optional<Escuela> obtenerPorId(Integer id) {
        return escuelaRepository.findById(id);
    }

    public Escuela guardar(Escuela escuela) {
        return escuelaRepository.save(escuela);
    }

    public void eliminar(Integer id) {
        escuelaRepository.deleteById(id);
    }

    public List<Trabajador> obtenerCapacitadores() {
        List<Trabajador> lista = trabajadorRepository.findByCargoNombreContainingIgnoreCase("capacitaci");
        System.out.println("Capacitadores encontrados: " + lista.size());
        for (Trabajador t : lista) {
            System.out.println(t.getIdTrabajador() + " - " + t.getNombreCompleto() + " - " + (t.getCargo() != null ? t.getCargo().getNombreCargo() : "SIN CARGO"));
        }
        return lista;
    }

    public List<Escuela> buscarPorNombre(String nombre) {
        return escuelaRepository.findByNombreEscuelaContainingIgnoreCase(nombre);
    }
}
