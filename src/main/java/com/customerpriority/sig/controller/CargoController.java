package com.customerpriority.sig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.customerpriority.sig.model.Cargo;
import com.customerpriority.sig.service.CargoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/cargos")
public class CargoController {
    
    @Autowired
    private CargoService cargoService;

    @GetMapping
    public String listarCargos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Cargo> cargoPage;

        if (search != null && !search.isEmpty()) {
            // Ejecutar búsqueda en los campos segmento, nombreCampana y gestion
            cargoPage = cargoService.buscarCargosPorKeyword(search, pageable);
        } else {
            // Listar todas las campañas paginadas
            cargoPage = cargoService.listarCargosPaginados(pageable);
        }

        model.addAttribute("cargoPage", cargoPage);
        model.addAttribute("search", search); // Mantener el valor de búsqueda en el campo
        return "cargos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Cargo cargo = new Cargo();
        model.addAttribute("cargo", cargo);
        return "cargos/formulario";
    }

    @PostMapping
    public String guardarCargo(@ModelAttribute("cargo") @Valid Cargo cargo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores, volvemos al formulario
            return "cargos/formulario";
        }
        
        // Si no hay errores, guardamos la campaña
        cargoService.guardarCargo(cargo);
        return "redirect:/cargos";
    }    

    
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
    try {
        Cargo cargo = cargoService.obtenerCargoPorId(id);
        model.addAttribute("cargo", cargo);
        return "cargos/formulario";
    } catch (EntityNotFoundException e) {
        // Manejar el caso donde no se encuentre la campaña
        return "redirect:/cargos?error=notfound";
    }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarCampana(@PathVariable int id) {
        cargoService.eliminarCargo(id);
        return "redirect:/cargos";
    }
}