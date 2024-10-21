package com.customerpriority.sig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.customerpriority.sig.model.Campana;
import com.customerpriority.sig.service.CampanaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/campanas")
public class CampanaController {
    
    @Autowired
    private CampanaService campanaService;

    @GetMapping
    public String listarCampanas(Model model){
        model.addAttribute("campanas", campanaService.listarTodasLasCampanas());
        return "campanas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Campana campana = new Campana();
        campana.setEstado(1); // Esto asegurará que el estado por defecto sea 1
        model.addAttribute("campana", campana);
        return "campanas/formulario";
    }

    @PostMapping
    public String guardarCampana(@ModelAttribute("campana") @Valid Campana campana, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores, volvemos al formulario
            return "campanas/formulario";
        }
        
        // Si no hay errores, guardamos la campaña
        campanaService.guardarCampana(campana);
        return "redirect:/campanas";
    }    

    
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
    try {
        Campana campana = campanaService.obtenerCampanaPorId(id);
        model.addAttribute("campana", campana);
        return "campanas/formulario";
    } catch (EntityNotFoundException e) {
        // Manejar el caso donde no se encuentre la campaña
        return "redirect:/campanas?error=notfound";
    }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarCampana(@PathVariable int id) {
        campanaService.eliminarCampana(id);
        return "redirect:/campanas";
    }
}