package com.customerpriority.sig.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.customerpriority.sig.model.Departamento;
import com.customerpriority.sig.model.Distrito;
import com.customerpriority.sig.model.Provincia;
import com.customerpriority.sig.service.UbicacionService;

@Controller
@RequestMapping("/ubicaciones")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    // Obtener todos los departamentos
    @GetMapping("/departamentos")
    @ResponseBody
    public List<Departamento> obtenerDepartamentos() {
        return ubicacionService.obtenerDepartamentos();
    }

    // Obtener provincias según el departamento
    @GetMapping("/provincias/{idDepartamento}")
    @ResponseBody
    public List<Provincia> obtenerProvinciasPorDepartamento(@PathVariable int idDepartamento) {
        return ubicacionService.obtenerProvinciasPorDepartamento(idDepartamento);
    }

    // Obtener distritos según la provincia
    @GetMapping("/distritos/{idProvincia}")
    @ResponseBody
    public List<Distrito> obtenerDistritosPorProvincia(@PathVariable int idProvincia) {
        return ubicacionService.obtenerDistritosPorProvincia(idProvincia);
    }
}