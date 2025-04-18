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

import com.customerpriority.sig.model.Segmento;
import com.customerpriority.sig.service.CampanaService;
import com.customerpriority.sig.service.ExcelExportService;
import com.customerpriority.sig.service.SegmentoService;
import com.customerpriority.sig.service.TipoGestionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/segmentos")
public class SegmentoController {
    
    @Autowired
    private SegmentoService segmentoService;

    @Autowired
    private CampanaService campanaService;

    @Autowired
    private TipoGestionService tipoGestionService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public String listarSegmentos(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Campanas
        Page<Segmento> segmentoPage;
        if (keyword != null && !keyword.isEmpty()) {
            segmentoPage = segmentoService.buscarSegmentosPorKeyword(keyword, pageable);
        } else {
            segmentoPage = segmentoService.listarSegmentosPaginados(pageable);
        }

        model.addAttribute("segmentoPage", segmentoPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "segmentos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Segmento segmento = new Segmento();
        segmento.setEstado(1); // Esto asegurará que el estado por defecto sea 1
        model.addAttribute("segmento", segmento);
        model.addAttribute("campanas", campanaService.listarTodasLasCampanas());
        model.addAttribute("tipoGestion", tipoGestionService.listarTodasLasGestiones());
        return "segmentos/formulario";
    }

    @PostMapping
    public String guardarSegmento(@ModelAttribute @Valid Segmento segmento, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Si hay errores, volvemos al formulario
            return "segmentos/formulario";
        }
        
        // Si no hay errores, guardamos la campaña
        segmentoService.guardarSegmento(segmento);
        return "redirect:/segmentos";
    }    

    
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
    try {
        Segmento segmento = segmentoService.obtenerSegmentoPorId(id);
        model.addAttribute("segmento", segmento);
        model.addAttribute("campanas", campanaService.listarTodasLasCampanas());
        model.addAttribute("tipoGestion", tipoGestionService.listarTodasLasGestiones());
        return "segmentos/formulario";
    } catch (EntityNotFoundException e) {
        // Manejar el caso donde no se encuentre la campaña
        return "redirect:/segmentos?error=notfound";
    }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarCampana(@PathVariable int id) {
        segmentoService.eliminarSegmento(id);
        return "redirect:/segmentos";
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarSegmentosAExcel() throws IOException {
        List<Segmento> segmentos = segmentoService.listarTodosLosSegmentos();
        ByteArrayInputStream bais = excelExportService.exportarSegmentosAExcel(segmentos);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=segmentos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }


    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarSegmentosAExcel(
            @RequestParam(required = false) String keyword) throws IOException {
        
        List<Segmento> segmentos;
    
        if (keyword != null && !keyword.isEmpty()) {
            // Exportar solo las campañas filtradas por el keyword
            segmentos = segmentoService.buscarSegmentosPorKeyword(keyword, Pageable.unpaged()).getContent();
        } else {
            // Exportar todas las campañas
            segmentos = segmentoService.listarTodosLosSegmentos();
        }
    
        ByteArrayInputStream bais = excelExportService.exportarSegmentosAExcel(segmentos);
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=segmentos.xlsx");
    
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }

}