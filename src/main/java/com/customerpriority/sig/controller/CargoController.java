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

import com.customerpriority.sig.model.Cargo;
import com.customerpriority.sig.service.CargoService;
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
@RequestMapping("/cargos")
public class CargoController {
    
    @Autowired
    private CargoService cargoService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public String listarCargos(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Cargo> cargoPage;

        if (keyword != null && !keyword.isEmpty()) {
            cargoPage = cargoService.buscarCargosPorKeyword(keyword, pageable);
        } else {
            cargoPage = cargoService.listarCargosPaginados(pageable);
        }


        model.addAttribute("cargoPage", cargoPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "cargos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Cargo cargo = new Cargo();
        model.addAttribute("cargo", cargo);
        return "cargos/formulario";
    }

    @PostMapping
    public String guardarCargo(@ModelAttribute @Valid Cargo cargo, BindingResult result, Model model) {
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


    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarCargosAExcel(
            @RequestParam(required = false) String keyword) throws IOException {
        
        List<Cargo> cargos;
    
        if (keyword != null && !keyword.isEmpty()) {
            // Exportar solo las campañas filtradas por el keyword
            cargos = cargoService.buscarCargosPorKeyword(keyword, Pageable.unpaged()).getContent();
        } else {
            // Exportar todas las campañas
            cargos = cargoService.listarTodosLosCargos();
        }
    
        ByteArrayInputStream bais = excelExportService.exportarCargosAExcel(cargos);
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=cargos.xlsx");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }
}