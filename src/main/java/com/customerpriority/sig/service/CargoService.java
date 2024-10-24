package com.customerpriority.sig.service;

import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.customerpriority.sig.model.Cargo;
import com.customerpriority.sig.repository.CargoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargo> listarTodosLosCargos(){
        return cargoRepository.findAll();
    }

    public Page<Cargo> listarCargosPaginados(Pageable pageable){
        return cargoRepository.findAll(pageable);
    }

    public Page<Cargo> buscarCargosPorKeyword(String keyword, Pageable pageable) {
        return cargoRepository.buscarPorCargo(keyword, pageable);
    }

    public Cargo obtenerCargoPorId(int id) {
        return cargoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cargo no encontrado"));
    }

    public void guardarCargo(Cargo cargo){
        cargoRepository.save(cargo);
    }

    public void eliminarCargo(int id){
        cargoRepository.deleteById(id);
    }

}
