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

import com.customerpriority.sig.model.Centro;
import com.customerpriority.sig.service.CentroService;
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
@RequestMapping("/centros")
public class CentroController {
    
    @Autowired
    private CentroService centroService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public String listarCentros(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Centro> centroPage;

        if (keyword != null && !keyword.isEmpty()) {
            centroPage = centroService.buscarCentrosPorKeyword(keyword, pageable);
        } else {
            centroPage = centroService.listarCentrosPaginados(pageable);
        }


        model.addAttribute("centroPage", centroPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "centros/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Centro centro = new Centro();
        model.addAttribute("centro", centro);
        return "centros/formulario";
    }

    @PostMapping
    public String guardarCentro(@ModelAttribute @Valid Centro centro, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores, volvemos al formulario
            return "centros/formulario";
        }
        
        // Si no hay errores, guardamos la campaña
        centroService.guardarCentro(centro);
        return "redirect:/centros";
    }    

    
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
    try {
        Centro centro = centroService.obtenerCentroPorId(id);
        model.addAttribute("centro", centro);
        return "centros/formulario";
    } catch (EntityNotFoundException e) {
        // Manejar el caso donde no se encuentre el centro
        return "redirect:/centros?error=notfound";
    }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarCentro(@PathVariable int id) {
        centroService.eliminarCentro(id);
        return "redirect:/centros";
    }


    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarCentrosAExcel(
            @RequestParam(required = false) String keyword) throws IOException {
        
        List<Centro> centros;
    
        if (keyword != null && !keyword.isEmpty()) {
            // Exportar solo las campañas filtradas por el keyword
            centros = centroService.buscarCentrosPorKeyword(keyword, Pageable.unpaged()).getContent();
        } else {
            // Exportar todas las campañas
            centros = centroService.listarTodosLosCentros();
        }
    
        ByteArrayInputStream bais = excelExportService.exportarCentrosAExcel(centros);
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=centros.xlsx");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }
}