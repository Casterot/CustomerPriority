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

import com.customerpriority.sig.model.Empresa;
import com.customerpriority.sig.service.EmpresaService;
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
@RequestMapping("/empresas")
public class EmpresaController {
    
    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public String listarEmpresas(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Empresa> empresaPage;

        if (keyword != null && !keyword.isEmpty()) {
            empresaPage = empresaService.buscarEmpresasPorKeyword(keyword, pageable);
        } else {
            empresaPage = empresaService.listarEmpresasPaginadas(pageable);
        }


        model.addAttribute("empresaPage", empresaPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "empresas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Empresa empresa = new Empresa();
        model.addAttribute("empresa", empresa);
        return "empresas/formulario";
    }

    @PostMapping
    public String guardarEmpresa(@ModelAttribute @Valid Empresa empresa, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores, volvemos al formulario
            return "empresas/formulario";
        }
        
        // Si no hay errores, guardamos la empresa
        empresaService.guardarEmpresa(empresa);
        return "redirect:/empresas";
    }    

    
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
    try {
        Empresa empresa = empresaService.obtenerEmpresaPorId(id);
        model.addAttribute("empresa", empresa);
        return "empresas/formulario";
    } catch (EntityNotFoundException e) {
        // Manejar el caso donde no se encuentre la empresa
        return "redirect:/empresas?error=notfound";
    }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpresa(@PathVariable int id) {
        empresaService.eliminarEmpresa(id);
        return "redirect:/empresas";
    }


    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarEmpresasAExcel(
            @RequestParam(required = false) String keyword) throws IOException {
        
        List<Empresa> empresas;
    
        if (keyword != null && !keyword.isEmpty()) {
            // Exportar solo las empresas filtradas por el keyword
            empresas = empresaService.buscarEmpresasPorKeyword(keyword, Pageable.unpaged()).getContent();
        } else {
            // Exportar todas las campañas
            empresas = empresaService.listarTodasLasEmpresas();
        }
    
        ByteArrayInputStream bais = excelExportService.exportarEmpresasAExcel(empresas);
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=empresa.xlsx");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }
}