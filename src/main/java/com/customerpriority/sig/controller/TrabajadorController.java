package com.customerpriority.sig.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.customerpriority.sig.model.Cargo;
import com.customerpriority.sig.model.Jornada;
import com.customerpriority.sig.model.TipoGestion;
import com.customerpriority.sig.model.Trabajador;
import com.customerpriority.sig.service.TrabajadorService;
import com.customerpriority.sig.service.SegmentoService;
import com.customerpriority.sig.service.TipoContratoService;
import com.customerpriority.sig.service.CampanaService;
import com.customerpriority.sig.service.CargoService;
import com.customerpriority.sig.service.CentroService;
import com.customerpriority.sig.service.CondicionService;
import com.customerpriority.sig.service.DepartamentoService;
import com.customerpriority.sig.service.DistritoService;
import com.customerpriority.sig.service.EmpresaService;
import com.customerpriority.sig.service.ExcelExportService;
import com.customerpriority.sig.service.GeneroService;
import com.customerpriority.sig.service.HorarioService;
import com.customerpriority.sig.service.JornadaService;
import com.customerpriority.sig.service.ModalidadService;
import com.customerpriority.sig.service.ProvinciaService;
import com.customerpriority.sig.service.TipoDocumentoService;
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

    @Autowired
    private CentroService centroService;

    @Autowired
    private CargoService cargoService;

    @Autowired
    private JornadaService jornadaService;

    @Autowired
    private ModalidadService modalidadService;

    @Autowired
    private CampanaService campanaService;

    @Autowired
    private TipoGestionService tipoGestionService;

    @Autowired
    private TipoContratoService tipoContratoService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private HorarioService horarioService;

    @GetMapping
    public String listarTrabajadores(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(value = "search", required = false) String keyword) {

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
        Jornada jornadaPorDefecto = jornadaService.obtenerJornadaPorId(2);
        trabajador.setJornada(jornadaPorDefecto);
        trabajador.setEstado("Activo"); // Establece el valor por defecto

        // Selecciona por defecto el cargo "ASESOR TELEFÓNICO"
        List<Cargo> cargos = cargoService.listarTodosLosCargos();
        for (Cargo cargo : cargos) {
            if ("ASESOR TELEFÓNICO".equalsIgnoreCase(cargo.getNombreCargo())) {
                trabajador.setCargo(cargo);
                break;
            }
        }

        model.addAttribute("trabajador", trabajador);
        cargarDatosComunes(model, null, trabajador);
        return "trabajadores/formulario";
    }

    @PostMapping
    public String guardarTrabajador(@ModelAttribute @Valid Trabajador trabajador, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            cargarDatosComunes(model, trabajador, trabajador);
            return "trabajadores/formulario";
        }

        System.out.println("DEBUG apellidoMaterno recibido: '" + trabajador.getApellidoMaterno() + "'");
        try {
            trabajadorService.guardarTrabajador(trabajador);
        } catch (IllegalArgumentException e) {
            result.rejectValue("correo", "error.trabajador", e.getMessage());
            cargarDatosComunes(model, trabajador, trabajador);
            return "trabajadores/formulario";
        }

        return "redirect:/trabajadores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable int id, Model model) {
        try {
            Trabajador trabajador = trabajadorService.obtenerTrabajadorPorId(id);
            model.addAttribute("trabajador", trabajador);
            cargarDatosComunes(model, trabajador, trabajador);
            return "trabajadores/formulario";
        } catch (EntityNotFoundException e) {
            return "redirect:/trabajadores?error=notfound";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTrabajador(@PathVariable int id) {
        trabajadorService.eliminarTrabajador(id);
        return "redirect:/trabajadores";
    }

    /** Cargar datos comunes */

    private void cargarDatosComunes(Model model, Trabajador trabajador, Trabajador referencia) {
        model.addAttribute("tipoDocumento", tipoDocumentoService.listarTodosLosDocumentos());
        model.addAttribute("condiciones", condicionService.listarTodasLasCondiciones());
        model.addAttribute("genero", generoService.listarTodosLosGeneros());
        model.addAttribute("departamentos", departamentoService.listarTodosLosDepartamentos());
        model.addAttribute("centro", centroService.listarTodosLosCentros());
        model.addAttribute("jornada", jornadaService.listarTodasLasJornadas());
        model.addAttribute("modalidad", modalidadService.listarTodasLasModalidades());
        model.addAttribute("tipoContrato", tipoContratoService.listarTodosLosContratos());
        model.addAttribute("empresa", empresaService.listarTodasLasEmpresas());
        model.addAttribute("horarios", horarioService.listarTodosLosHorarios());

        // Ordenar campañas
        List<Campana> campanas = campanaService.listarTodasLasCampanas();
        campanas.sort(Comparator.comparing(Campana::getNombreCampana));
        model.addAttribute("campanas", campanas);

        // Ordenar cargos
        List<Cargo> cargos = cargoService.listarTodosLosCargos();
        cargos.sort(Comparator.comparing(Cargo::getNombreCargo));
        model.addAttribute("cargos", cargos);

        // Configurar segmentos y gestiones
        // Cargar segmentos y gestiones basados en la referencia
        if (referencia != null && referencia.getSegmento() != null) {
            model.addAttribute("segmentos",
                    segmentoService.listarSegmentosPorCampana(referencia.getSegmento().getCampana().getIdCampana()));
            List<TipoGestion> gestiones = tipoGestionService
                    .listarGestionesPorSegmento(referencia.getSegmento().getIdSegmento());
            model.addAttribute("gestiones", gestiones);
        } else {
            // Mostrar solo segmentos activos si es un nuevo trabajador
            model.addAttribute("segmentos", segmentoService.listarSegmentosActivos());
            model.addAttribute("gestiones", Collections.emptyList());
        }

        // Configurar provincias y distritos
        if (referencia.getDistrito() != null && referencia.getDistrito().getProvincia() != null) {
            model.addAttribute("provincias", provinciaService.listarProvinciasPorDepartamento(
                    referencia.getDistrito().getProvincia().getDepartamento().getIdDepartamento()));
            model.addAttribute("distritos", distritoService.listarDistritosPorProvincia(
                    referencia.getDistrito().getProvincia().getIdProvincia()));
        } else {
            model.addAttribute("provincias", Collections.emptyList());
            model.addAttribute("distritos", Collections.emptyList());
        }

        // Configurar lista de trabajadores excluyendo subordinados
        // Obtener todos los trabajadores registrados
        List<Trabajador> trabajadores = trabajadorService.listarTodosLosTrabajadores();

        // Crear nueva lista de trabajadores filtrados
        List<Trabajador> trabajadoresFiltrados = new ArrayList<>();

        if (referencia != null && referencia.getIdTrabajador() > 0) {
            List<Trabajador> subordinados = trabajadorService.obtenerTodosLosSubordinados(referencia);

            if (subordinados == null) {
                subordinados = new ArrayList<>();
            }


            for (Trabajador t : trabajadores) {
                // Verificar si el trabajador no es el mismo de referencia, no es subordinado y su cargo tiene "Si" en personalCargo    
                if (t.getIdTrabajador() != referencia.getIdTrabajador()
                    && !subordinados.contains(t)
                    && t.getCargo() != null
                    && "Si".equalsIgnoreCase(t.getCargo().getPersonalCargo())) {
                    trabajadoresFiltrados.add(t);
                }
            }

            
        } else{
            // Si es un nuevo trabajador, filtrar solo los que tienen "Si" en personalCargo
            for (Trabajador t : trabajadores) {
                if (t.getCargo() != null && "Si".equalsIgnoreCase(t.getCargo().getPersonalCargo())) {
                    trabajadoresFiltrados.add(t);
                }
            }
        }

        // Ordenar la lista final de trabajadores
        trabajadoresFiltrados.sort(Comparator.comparing(
                t -> (t.getApellidoPaterno() + " " + t.getApellidoMaterno() + " " + t.getNombreCompleto())));
        model.addAttribute("trabajadores", trabajadoresFiltrados);

    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarTrabajadoresAExcel() throws IOException {
        List<Trabajador> trabajadores = trabajadorService.listarTodosLosTrabajadores();
        ByteArrayInputStream bais = excelExportService.exportarTrabajadoresAExcel(trabajadores);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=nomina.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarTrabajadoresAExcel(
            @RequestParam(required = false) String keyword) throws IOException {

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
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bais.readAllBytes());
    }

}
