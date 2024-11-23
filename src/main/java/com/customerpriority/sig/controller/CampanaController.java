package com.customerpriority.sig.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

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

import com.customerpriority.sig.model.Campana;

import com.customerpriority.sig.service.CampanaService;
import com.customerpriority.sig.service.ExcelExportService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/campanas")
public class CampanaController {
    
    @Autowired
    private CampanaService campanaService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public String listarCampanas(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Campana> campanaPage;

        if (keyword != null && !keyword.isEmpty()) {
            campanaPage = campanaService.buscarCampanasPorKeyword(keyword, pageable);
        } else {
            campanaPage = campanaService.listarCampanasPaginadas(pageable);
        }


        model.addAttribute("campanaPage", campanaPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "campanas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Campana campana = new Campana();
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


    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarCampanasAExcel(
            @RequestParam(value = "keyword", required = false) String keyword) throws IOException {
        
        List<Campana> campanas;
    
        if (keyword != null && !keyword.isEmpty()) {
            // Exportar solo las campañas filtradas por el keyword
            campanas = campanaService.buscarCampanasPorKeyword(keyword, Pageable.unpaged()).getContent();
        } else {
            // Exportar todas las campañas
            campanas = campanaService.listarTodasLasCampanas();
        }
    
        ByteArrayInputStream bais = excelExportService.exportarCampanasAExcel(campanas);
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=campanas.xlsx");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }
}