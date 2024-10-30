package com.customerpriority.sig.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Nombre de la vista de Thymeleaf
    }

    @GetMapping("/index") //Antes Home
    public String index() {
        return "index"; // Página después del inicio de sesión
    }
}