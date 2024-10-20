package com.customerpriority.sig.controller;

import com.customerpriority.sig.model.Usuario;
import com.customerpriority.sig.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;




@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios"; // Nombre de la vista para mostrar la lista
    }

    @GetMapping("/nuevo")
    public String crearUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "form"; // Nombre de la vista para el formulario de creación
    }

    @PostMapping
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.save(usuario);        
        return "redirect:/usuarios";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        model.addAttribute("usuario", usuarioService.findById(id).get());
        return "formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id) {
        usuarioService.delete(id);
        return "redirect:/usuarios";
    }
    
}