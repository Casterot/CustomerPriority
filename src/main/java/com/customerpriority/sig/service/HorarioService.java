package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Horario;
import com.customerpriority.sig.repository.HorarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    public List<Horario> listarTodosLoshorarios(){
        return horarioRepository.findAll();
    }

    public Page<Horario> listarHorariosPaginados(Pageable pageable){
        return horarioRepository.findAll(pageable);
    }

    public Page<Horario> buscarHorariosPorKeyword(String keyword, Pageable pageable) {
        return horarioRepository.buscarPorHorario(keyword, pageable);
    }

    public Horario obtenerHorarioPorId(int id) {
        return horarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Horario no encontrado"));
    }

    public void guardarHorario(Horario horario){
        horarioRepository.save(horario);
    }

    public void eliminarHorario(int id){
        horarioRepository.deleteById(id);
    }

}
