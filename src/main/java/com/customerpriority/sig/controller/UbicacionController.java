package com.customerpriority.sig.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/provincias/{idDepartamento}")
    @ResponseBody
    public List<Map<String, String>> obtenerProvinciasPorDepartamento(@PathVariable int idDepartamento) {
        List<Provincia> provincias = ubicacionService.obtenerProvinciasPorDepartamento(idDepartamento);
        return provincias.stream().map(provincia -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(provincia.getIdProvincia()));
            map.put("name", provincia.getProvincia());
            return map;
        }).collect(Collectors.toList());
    }
    
    @GetMapping("/distritos/{idProvincia}")
    @ResponseBody
    public List<Map<String, String>> obtenerDistritosPorProvincia(@PathVariable int idProvincia) {
        List<Distrito> distritos = ubicacionService.obtenerDistritosPorProvincia(idProvincia);
        return distritos.stream().map(distrito -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(distrito.getIdDistrito()));
            map.put("name", distrito.getDistrito());
            return map;
        }).collect(Collectors.toList());
    }
    

}
