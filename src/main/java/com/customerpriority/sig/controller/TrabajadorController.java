package com.customerpriority.sig.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
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

import com.customerpriority.sig.model.Trabajador;
import com.customerpriority.sig.service.TrabajadorService;
import com.customerpriority.sig.service.SegmentoService;
import com.customerpriority.sig.service.CondicionService;
import com.customerpriority.sig.service.DepartamentoService;
import com.customerpriority.sig.service.DistritoService;
import com.customerpriority.sig.service.ExcelExportService;
import com.customerpriority.sig.service.GeneroService;
import com.customerpriority.sig.service.ProvinciaService;
import com.customerpriority.sig.service.TipoDocumentoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/trabajadores")
public class TrabajadorController {
    
    @Autowired
    private TrabajadorService trabajadorService;

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @Autowired
    private SegmentoService segmentoService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private CondicionService condicionService;
    
    @Autowired
    private GeneroService generoService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private DistritoService distritoService;

    @GetMapping
    public String listarTrabajadores(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Trabajador> trabajadorPage;
        if (keyword != null && !keyword.isEmpty()) {
            trabajadorPage = trabajadorService.buscarTrabajadoresPorKeyword(keyword, pageable);
        } else {
            trabajadorPage = trabajadorService.listarTrabajadoresPaginados(pageable);
        }

        model.addAttribute("trabajadorPage", trabajadorPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "trabajadores/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Trabajador trabajador = new Trabajador();
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("tipoDocumento", tipoDocumentoService.listarTodosLosDocumentos()); // Obtén los tipos desde el servicio
        model.addAttribute("segmento", segmentoService.listarTodosLosSegmentos()); // Obtén los tipos desde el servicio
        model.addAttribute("condiciones", condicionService.listarTodasLasCondiciones()); // Obtén los tipos desde el servicio
        model.addAttribute("genero", generoService.listarTodosLosGeneros()); // Obtén los tipos desde el servicio
        model.addAttribute("departamentos", departamentoService.listarTodosLosDepartamentos());
        // Inicialmente no hay provincias ni distritos porque es un nuevo registro
        model.addAttribute("provincias", Collections.emptyList());
        model.addAttribute("distritos", Collections.emptyList());
        return "trabajadores/formulario";
    }

    @PostMapping
    public String guardarTrabajador(@ModelAttribute("trabajador") @Valid Trabajador trabajador, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores, volvemos al formulario
            return "trabajadores/formulario";
        }
        
        // Si no hay errores, guardamos el trabajador
        trabajadorService.guardarTrabajador(trabajador);
        return "redirect:/trabajadores";
    }   

    
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
    try {
        Trabajador trabajador = trabajadorService.obtenerTrabajadorPorId(id);
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("tipoDocumento", tipoDocumentoService.listarTodosLosDocumentos()); // Obtén los tipos desde el servicio
        model.addAttribute("segmento", segmentoService.listarTodosLosSegmentos()); // Obtén los tipos desde el servicio
        model.addAttribute("condiciones", condicionService.listarTodasLasCondiciones());
        model.addAttribute("genero", generoService.listarTodosLosGeneros());
        //model.addAttribute("departamentos", ubicacionService.obtenerDepartamentos());
        model.addAttribute("departamentos", departamentoService.listarTodosLosDepartamentos());
        model.addAttribute("provincias", provinciaService.listarProvinciasPorDepartamento(trabajador.getDistrito().getProvincia().getDepartamento().getIdDepartamento()));
        model.addAttribute("distritos", distritoService.listarDistritosPorProvincia(trabajador.getDistrito().getProvincia().getIdProvincia()));
        return "trabajadores/formulario";
    } catch (EntityNotFoundException e) {
        // Manejar el caso donde no se encuentre el trabajador
        return "redirect:/trabajadores?error=notfound";
    }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarTrabajador(@PathVariable int id) {
        trabajadorService.eliminarTrabajador(id);
        return "redirect:/trabajadores";
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarTrabajadoresAExcel() throws IOException {
        List<Trabajador> trabajadores = trabajadorService.listarTodosLosTrabajadores();
        ByteArrayInputStream bais = excelExportService.exportarTrabajadoresAExcel(trabajadores);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=nomina.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }


    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarTrabajadoresAExcel(
            @RequestParam(value = "keyword", required = false) String keyword) throws IOException {
        
        List<Trabajador> trabajadores;
    
        if (keyword != null && !keyword.isEmpty()) {
            // Exportar solo las campañas filtradas por el keyword
            trabajadores = trabajadorService.buscarTrabajadoresPorKeyword(keyword, Pageable.unpaged()).getContent();
        } else {
            // Exportar todas las campañas
            trabajadores = trabajadorService.listarTodosLosTrabajadores();
        }
    
        ByteArrayInputStream bais = excelExportService.exportarTrabajadoresAExcel(trabajadores);
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Nomina.xlsx");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }

}