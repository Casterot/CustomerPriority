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

import com.customerpriority.sig.model.Condicion;
import com.customerpriority.sig.model.Horario;
import com.customerpriority.sig.model.Turno;
import com.customerpriority.sig.service.HorarioService;
import com.customerpriority.sig.service.TurnoService;
import com.customerpriority.sig.service.CondicionService;
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
@RequestMapping("/horarios")
public class HorarioController {
    
    @Autowired
    private HorarioService horarioService;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private CondicionService condicionService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public String listarHorarios(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Horario> horarioPage;
        if (keyword != null && !keyword.isEmpty()) {
            horarioPage = horarioService.buscarHorariosPorKeyword(keyword, pageable);
        } else {
            horarioPage = horarioService.listarHorariosPaginados(pageable);
        }

        model.addAttribute("horarioPage", horarioPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "horarios/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Horario horario = new Horario();
        model.addAttribute("horario", horario);
        model.addAttribute("turnos", turnoService.listarTodosLosTurnos());
        model.addAttribute("condiciones", condicionService.listarTodasLasCondiciones());
        return "horarios/formulario";
    }

    @PostMapping
    public String guardarHorario(@ModelAttribute("horario") @Valid Horario horario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores, volvemos al formulario
            cargarTurnosYCondiciones(model);
            return "horarios/formulario";
        }
        
        // Si no hay errores, guardamos la campaña
        horarioService.guardarHorario(horario);
        return "redirect:/horarios";
    }

    
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
    try {
        Horario horario = horarioService.obtenerHorarioPorId(id);
        model.addAttribute("horario", horario);
        model.addAttribute("turnos", turnoService.listarTodosLosTurnos());
        model.addAttribute("condiciones", condicionService.listarTodasLasCondiciones());
        return "horarios/formulario";
    } catch (EntityNotFoundException e) {
        // Manejar el caso donde no se encuentre la campaña
        return "redirect:/horarios?error=notfound";
    }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarHorario(@PathVariable int id) {
        horarioService.eliminarHorario(id);
        return "redirect:/horarios";
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarCampanasAExcel() throws IOException {
        List<Horario> horarios = horarioService.listarTodosLoshorarios();
        ByteArrayInputStream bais = excelExportService.exportarHorariosAExcel(horarios);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=campanas.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }


    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarHorariosAExcel(
            @RequestParam(value = "keyword", required = false) String keyword) throws IOException {
        
        List<Horario> horarios;
    
        if (keyword != null && !keyword.isEmpty()) {
            // Exportar solo las campañas filtradas por el keyword
            horarios = horarioService.buscarHorariosPorKeyword(keyword, Pageable.unpaged()).getContent();
        } else {
            // Exportar todas las campañas
            horarios = horarioService.listarTodosLoshorarios();
        }
    
        ByteArrayInputStream bais = excelExportService.exportarHorariosAExcel(horarios);
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=horarios.xlsx");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }

    // Método auxiliar para cargar Turnos y Condiciones
    private void cargarTurnosYCondiciones(Model model) {
        List<Turno> turnos = turnoService.listarTodosLosTurnos();
        List<Condicion> condiciones = condicionService.listarTodasLasCondiciones();
        model.addAttribute("turnos", turnos);
        model.addAttribute("condiciones", condiciones);
    }
}