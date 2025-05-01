package com.customerpriority.sig.controller;

import com.customerpriority.sig.model.Usuario;
import com.customerpriority.sig.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cambiar-password")
public class CambiarPasswordController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String mostrarFormularioCambioPassword(Model model) {
        return "cambiar-password";
    }

    @PostMapping
    public String cambiarPassword(@RequestParam("passwordActual") String passwordActual,
                                  @RequestParam("nuevaPassword") String nuevaPassword,
                                  @RequestParam("confirmarPassword") String confirmarPassword,
                                  Model model, Authentication authentication) {
        if (!nuevaPassword.equals(confirmarPassword)) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "cambiar-password";
        }
        if (nuevaPassword.length() < 8) {
            model.addAttribute("error", "La nueva contraseña debe tener al menos 8 caracteres.");
            return "cambiar-password";
        }
        String username = authentication.getName();
        Usuario usuario = usuarioService.findByUsername(username).orElse(null);
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "cambiar-password";
        }
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            model.addAttribute("error", "La contraseña actual es incorrecta.");
            return "cambiar-password";
        }
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioService.save(usuario);
        model.addAttribute("mensaje", "Contraseña cambiada correctamente.");
        return "cambiar-password";
    }
}
