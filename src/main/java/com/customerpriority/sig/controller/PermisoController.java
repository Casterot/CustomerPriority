package com.customerpriority.sig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.customerpriority.sig.model.Permiso;
import com.customerpriority.sig.service.PermisoService;

@Controller
@RequestMapping("/permisos")
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @GetMapping
    public String listarPermisos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Permiso> permisoPage;
        
        if (search != null && !search.isEmpty()) {
            permisoPage = permisoService.buscarPermisos(search, pageable);
        } else {
            permisoPage = permisoService.listarTodos(pageable);
        }
        
        model.addAttribute("permisoPage", permisoPage);
        model.addAttribute("search", search);
        return "permisos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("permiso", new Permiso());
        return "permisos/formulario";
    }

    @PostMapping
    public String guardarPermiso(@ModelAttribute Permiso permiso) {
        permisoService.guardar(permiso);
        return "redirect:/permisos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Permiso permiso = permisoService.obtenerPorId(id);
        model.addAttribute("permiso", permiso);
        return "permisos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPermiso(@PathVariable Long id) {
        permisoService.eliminar(id);
        return "redirect:/permisos";
    }
}