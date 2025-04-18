package com.customerpriority.sig.controller;

import com.customerpriority.sig.model.Rol;
import com.customerpriority.sig.model.Usuario;
import com.customerpriority.sig.service.RolService;
import com.customerpriority.sig.service.UsuarioService;
import com.customerpriority.sig.service.ExcelExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public String listarUsuarios(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(value = "search", required = false) String keyword){

        int pageSize = 10; // Número de elementos por página

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page, pageSize);
        
        // Obtener el Page de Usuarios
        Page<Usuario> usuarioPage;

        if (keyword != null && !keyword.isEmpty()) {
            usuarioPage = usuarioService.buscarUsuariosPorKeyword(keyword, pageable);
        } else {
            usuarioPage = usuarioService.listarUsuariosPaginados(pageable);
        }

        model.addAttribute("usuarioPage", usuarioPage);
        model.addAttribute("search", keyword); // Mantener el valor de búsqueda en el campo
        return "usuarios/listar";
    }

    @GetMapping("/nuevo")
    public String crearUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "form"; // Nombre de la vista para el formulario de creación
    }

    @PostMapping
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.save(usuario);        
        return "redirect:/usuarios";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        Usuario usuario = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuarios/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id) {
        usuarioService.delete(id);
        return "redirect:/usuarios";
    }

    // Mostrar formulario de asignación de roles
    @GetMapping("/{id}/roles")
    public String mostrarFormularioAsignarRoles(@PathVariable int id, Model model) {
        Usuario usuario = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String nombreCompleto = "Usuario";
        if (usuario.getTrabajador() != null) {
            nombreCompleto = usuario.getTrabajador().getApellidoPaterno() + " " + 
                           usuario.getTrabajador().getApellidoMaterno() + ", " + 
                           usuario.getTrabajador().getNombreCompleto();
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("nombreCompleto", nombreCompleto);
        model.addAttribute("roles", rolService.listarTodosLosRoles());
        return "usuarios/asignar_roles";
    }
    
    // Asignar un rol al usuario
    @PostMapping("/{id}/roles")
    public String asignarRol(@PathVariable int id, @RequestParam int rolId) {
        Usuario usuario = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Rol rol = rolService.obtenerRolPorId(rolId);
        usuarioService.asignarRol(usuario, rol);
        return "redirect:/usuarios";
    }

    // Eliminar un rol del usuario
    @PostMapping("/{id}/roles/eliminar")
    public String eliminarRol(@PathVariable int id, @RequestParam int rolId) {
        Usuario usuario = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Rol rol = rolService.obtenerRolPorId(rolId);
        usuarioService.eliminarRol(usuario, rol);
        return "redirect:/usuarios";
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstado(@RequestParam int idUsuario, @RequestParam int estado) {
        Usuario usuario = usuarioService.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(estado);
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    @PostMapping("/restablecer-contrasena/{id}")
    public String restablecerContrasena(@PathVariable int id) {
        usuarioService.restablecerContrasenaADNI(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/exportar-excel")
    public void exportarExcel(@RequestParam(required = false) String keyword, HttpServletResponse response) {
        try {
            List<Usuario> usuarios;
            if (keyword != null && !keyword.trim().isEmpty()) {
                usuarios = usuarioService.buscarUsuariosPorKeyword(keyword);
            } else {
                usuarios = usuarioService.listarUsuarios();
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=usuarios.xlsx");

            ByteArrayInputStream in = excelExportService.exportarUsuariosAExcel(usuarios);
            byte[] bytes = in.readAllBytes();
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Guardar todos los roles asignados
    @PostMapping("/{id}/roles/guardar")
    @ResponseBody
    public ResponseEntity<?> guardarRoles(@PathVariable int id, @RequestBody Map<String, List<Integer>> request) {
        try {
            Usuario usuario = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            List<Integer> rolesIds = request.get("rolesIds");
            
            if (rolesIds == null) {
                return ResponseEntity.badRequest().body("No se proporcionaron IDs de roles");
            }
            
            // Limpiar roles existentes
            usuario.getRoles().clear();
            
            // Asignar nuevos roles
            for (Integer rolId : rolesIds) {
                try {
                    Rol rol = rolService.obtenerRolPorId(rolId);
                    if (rol != null) {
                        usuario.getRoles().add(rol);
                    }
                } catch (Exception e) {
                    // Continuar con el siguiente rol si hay un error con uno
                    System.err.println("Error al asignar rol " + rolId + ": " + e.getMessage());
                }
            }
            
            usuarioService.save(usuario);
            return ResponseEntity.ok().body("Roles guardados correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar los roles: " + e.getMessage());
        }
    }
}