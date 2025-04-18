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

import com.customerpriority.sig.model.Rol;
import com.customerpriority.sig.service.RolService;
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
@RequestMapping("/roles")
public class RolController {
    
    @Autowired
    private RolService rolService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public String listarRoles(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Rol> rolPage;

        if (keyword != null && !keyword.isEmpty()) {
            rolPage = rolService.buscarRolesPorKeyword(keyword, pageable);
        } else {
            rolPage = rolService.listarRolesPaginados(pageable);
        }


        model.addAttribute("rolPage", rolPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "roles/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Rol rol = new Rol();
        model.addAttribute("rol", rol);
        return "roles/formulario";
    }

    @PostMapping
    public String guardarRol(@ModelAttribute @Valid Rol rol, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores, volvemos al formulario
            return "roles/formulario";
        }
        
        // Si no hay errores, guardamos la campaña
        rolService.guardarRol(rol);
        return "redirect:/roles";
    }    

    
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
    try {
        Rol rol = rolService.obtenerRolPorId(id);
        model.addAttribute("rol", rol);
        return "roles/formulario";
    } catch (EntityNotFoundException e) {
        // Manejar el caso donde no se encuentre la campaña
        return "redirect:/roles?error=notfound";
    }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarRol(@PathVariable int id) {
        rolService.eliminarRol(id);
        return "redirect:/roles";
    }


    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarRolesAExcel(
            @RequestParam(required = false) String keyword) throws IOException {
        
        List<Rol> roles;
    
        if (keyword != null && !keyword.isEmpty()) {
            // Exportar solo las campañas filtradas por el keyword
            roles = rolService.buscarRolesPorKeyword(keyword, Pageable.unpaged()).getContent();
        } else {
            // Exportar todas las campañas
            roles = rolService.listarTodosLosRoles();
        }
    
        ByteArrayInputStream bais = excelExportService.exportarRolesAExcel(roles);
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=roles.xlsx");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }
}