package com.customerpriority.sig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.customerpriority.sig.service.CampanaService;

@Controller
@RequestMapping("/campana")
public class CampanaController {
    
    @Autowired
    private CampanaService service;

    @GetMapping
    public String listarCampanas(Model modelo){
        modelo.addAttribute("campana", service.listarTodasLasCampanas());
        return "campana";

    }

}