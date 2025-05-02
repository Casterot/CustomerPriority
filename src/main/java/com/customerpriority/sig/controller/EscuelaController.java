package com.customerpriority.sig.controller;

import com.customerpriority.sig.model.Escuela;
import com.customerpriority.sig.service.EscuelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/escuelas")
public class EscuelaController {
    @Autowired
    private EscuelaService escuelaService;

    @GetMapping
    public String listar(Model model, @RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("escuelas", escuelaService.buscarPorNombre(search));
        } else {
            model.addAttribute("escuelas", escuelaService.listarTodas());
        }
        model.addAttribute("search", search);
        return "escuelas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("escuela", new Escuela());
        model.addAttribute("capacitadores", escuelaService.obtenerCapacitadores());
        return "escuelas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Escuela escuela, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("escuela", escuela);
            model.addAttribute("capacitadores", escuelaService.obtenerCapacitadores());
            return "escuelas/formulario";
        }
        escuelaService.guardar(escuela);
        return "redirect:/escuelas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Escuela escuela = escuelaService.obtenerPorId(id).orElse(new Escuela());
        model.addAttribute("escuela", escuela);
        model.addAttribute("capacitadores", escuelaService.obtenerCapacitadores());
        return "escuelas/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        escuelaService.eliminar(id);
        return "redirect:/escuelas";
    }
}
