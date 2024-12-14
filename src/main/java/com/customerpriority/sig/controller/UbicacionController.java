package com.customerpriority.sig.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.customerpriority.sig.model.Campana;
import com.customerpriority.sig.model.Departamento;
import com.customerpriority.sig.model.Distrito;
import com.customerpriority.sig.model.Provincia;
import com.customerpriority.sig.model.Segmento;
import com.customerpriority.sig.model.TipoGestion;
import com.customerpriority.sig.service.CampanaService;
import com.customerpriority.sig.service.DepartamentoService;
import com.customerpriority.sig.service.DistritoService;
import com.customerpriority.sig.service.ProvinciaService;
import com.customerpriority.sig.service.SegmentoService;
import com.customerpriority.sig.service.TipoGestionService;

@Controller
@RequestMapping("/ubicaciones")
public class UbicacionController {

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private DistritoService distritoService;

    @Autowired
    private CampanaService campanaService;

    @Autowired
    private SegmentoService segmentoService;

    @Autowired
    private TipoGestionService tipoGestionService;

    // Obtener todos los departamentos
    @GetMapping("/departamentos")
    @ResponseBody
    public List<Departamento> obtenerDepartamentos() {
        return departamentoService.listarTodosLosDepartamentos();
    }

    @GetMapping("/provincias/{idDepartamento}")
    @ResponseBody
    public List<Map<String, String>> obtenerProvinciasPorDepartamento(@PathVariable int idDepartamento) {
        List<Provincia> provincias = provinciaService.listarProvinciasPorDepartamento(idDepartamento);
        return provincias.stream().map(provincia -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(provincia.getIdProvincia()));
            map.put("name", provincia.getProvincia());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/distritos/{idProvincia}")
    @ResponseBody
    public List<Map<String, String>> obtenerDistritosPorProvincia(@PathVariable int idProvincia) {
        List<Distrito> distritos = distritoService.listarDistritosPorProvincia(idProvincia);
        return distritos.stream().map(distrito -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(distrito.getIdDistrito()));
            map.put("name", distrito.getDistrito());
            return map;
        }).collect(Collectors.toList());
    }

    // Manejar las listas para campañas y segmentos

    // Obtener todas las campañas
    @GetMapping("/campanas")
    @ResponseBody
    public List<Campana> obtenerCampanas() {
        return campanaService.listarTodasLasCampanas();
    }

    @GetMapping("/segmentos/{idCampana}")
    @ResponseBody
    public List<Map<String, String>> obtenerSegmentosPorCampana(@PathVariable int idCampana) {
        List<Segmento> segmentos = segmentoService.listarSegmentosPorCampana(idCampana);

        // Ordenar segmentos alfabeticamente
        segmentos.sort(Comparator.comparing(Segmento::getNombreSegmento, String.CASE_INSENSITIVE_ORDER));

        return segmentos.stream().map(segmento -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(segmento.getIdSegmento()));
            map.put("name", segmento.getNombreSegmento());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/gestiones/{idSegmento}")
    @ResponseBody
    public List<Map<String, String>> obtenerTipoGestionPorSegmento(@PathVariable int idSegmento) {
        Segmento segmento = segmentoService.obtenerSegmentoPorId(idSegmento);
        int idGestion = segmento.getTipoGestion().getIdGestion();
        Optional<TipoGestion> tipoGestion = tipoGestionService.obtenerTipoGestionPorId(idGestion);
        List<Map<String, String>> resultado = new ArrayList<>();
        tipoGestion.ifPresent(tg -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(tg.getIdGestion()));
            map.put("name", tg.getNombreGestion());
            resultado.add(map);
        });
        return resultado;
    }

}
