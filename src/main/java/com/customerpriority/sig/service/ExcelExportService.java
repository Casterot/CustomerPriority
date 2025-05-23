package com.customerpriority.sig.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Segmento;
import com.customerpriority.sig.model.Cargo;
import com.customerpriority.sig.model.Centro;
import com.customerpriority.sig.model.Empresa;
import com.customerpriority.sig.model.Horario;
import com.customerpriority.sig.model.Rol;
import com.customerpriority.sig.model.Trabajador;
import com.customerpriority.sig.model.Campana;
import com.customerpriority.sig.model.Usuario;

@Service
public class ExcelExportService {
    public ByteArrayInputStream exportarSegmentosAExcel(List<Segmento> segmentos) throws IOException {
        String[] columnas = {"ID", "Campaña", "Segmento", "Gestión", "Estado"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Segmentos");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de las campañas
            int rowIdx = 1;
            for (Segmento segmento : segmentos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(segmento.getIdSegmento());
                row.createCell(1).setCellValue(segmento.getCampana().getNombreCampana());
                row.createCell(2).setCellValue(segmento.getNombreSegmento());
                row.createCell(3).setCellValue(segmento.getTipoGestion().getNombreGestion());
                row.createCell(4).setCellValue(segmento.getEstado());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    public ByteArrayInputStream exportarCargosAExcel(List<Cargo> cargos) throws IOException {
        String[] columnas = {"ID", "Cargo", "Personal a cargo"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Cargos");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de las campañas
            int rowIdx = 1;
            for (Cargo cargo : cargos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(cargo.getIdcargo());
                row.createCell(1).setCellValue(cargo.getNombreCargo());
                row.createCell(1).setCellValue(cargo.getPersonalCargo());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    public ByteArrayInputStream exportarHorariosAExcel(List<Horario> horarios) throws IOException {
        String[] columnas = {"ID", "Horario","Turno", "Condición"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Horarios");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de las campañas
            int rowIdx = 1;
            for (Horario horario : horarios) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(horario.getIdHorario());
                row.createCell(1).setCellValue(horario.getNombreHorario());
                row.createCell(2).setCellValue(horario.getTurno().getNombreTurno());
                row.createCell(3).setCellValue(horario.getCondicion().getNombreCondicion());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportarRolesAExcel(List<Rol> roles) throws IOException {
        String[] columnas = {"ID", "Rol"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Roles");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de las campañas
            int rowIdx = 1;
            for (Rol rol : roles) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(rol.getIdRol());
                row.createCell(1).setCellValue(rol.getNombreRol());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportarTrabajadoresAExcel(List<Trabajador> trabajadores) throws IOException {
        String[] columnas = {"ID", "Documento", "Apellido paterno", "Apellido materno", "Nombres", "Fecha de nacimiento",
            "Telefono", "Correo", "Direccion", "Distrito", "Provincia", "Departamento", "Genero", "Tipo de documento", "Campaña",
            "Jefe directo", "Horario", "Centro", "Modalidad", "Jornada","Cargo"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Trabajadores");

            // Crear un estilo para las fechas
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            
            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de las campañas
            int rowIdx = 1;
            for (Trabajador trabajador : trabajadores) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(trabajador.getIdTrabajador());
                row.createCell(1).setCellValue(trabajador.getDocumento());
                row.createCell(2).setCellValue(trabajador.getApellidoPaterno());
                row.createCell(3).setCellValue(trabajador.getApellidoMaterno());
                row.createCell(4).setCellValue(trabajador.getNombreCompleto());

                // Asignar el valor de fecha y aplicar el estilo
                Cell fechaNacimientoCell = row.createCell(5);
                if (trabajador.getFechaNacimiento() != null) {
                    fechaNacimientoCell.setCellValue(trabajador.getFechaNacimiento());
                    fechaNacimientoCell.setCellStyle(dateCellStyle);
                } else {
                    fechaNacimientoCell.setCellValue("Sin fecha");
                }

                row.createCell(6).setCellValue(trabajador.getTelefono() != null ? trabajador.getTelefono() : "");
                row.createCell(7).setCellValue(trabajador.getCorreo() != null ? trabajador.getCorreo() : "");
                row.createCell(8).setCellValue(trabajador.getDireccion() != null ? trabajador.getDireccion() : "");
                row.createCell(9).setCellValue(trabajador.getDistrito() != null ? trabajador.getDistrito().getDistrito() : "Sin distrito");
                row.createCell(10).setCellValue(trabajador.getDistrito() != null ? trabajador.getDistrito().getProvincia().getProvincia() : "Sin provincia");
                row.createCell(11).setCellValue(trabajador.getDistrito() != null ? trabajador.getDistrito().getProvincia().getDepartamento().getDepartamento() : "Sin departamento");
                row.createCell(12).setCellValue(trabajador.getGenero() != null ? trabajador.getGenero().getNombreGenero() : "Sin género");
                row.createCell(13).setCellValue(trabajador.getTipoDocumento() != null ? trabajador.getTipoDocumento().getNombreTipoDocumento() : "Sin tipo de documento");
                row.createCell(14).setCellValue(trabajador.getSegmento() != null ? trabajador.getSegmento().getNombreSegmento() : "Sin campaña");
                row.createCell(15).setCellValue(trabajador.getJefeDirecto() != null ? trabajador.getJefeDirecto().getApellidoPaterno() : "Sin jefe");
                row.createCell(16).setCellValue(trabajador.getHorario() != null ? trabajador.getHorario().getNombreHorario() : "Sin horario");
                row.createCell(17).setCellValue(trabajador.getCentro() != null ? trabajador.getCentro().getNombreCentro() : "Sin centro");
                row.createCell(18).setCellValue(trabajador.getModalidad() != null ? trabajador.getModalidad().getNombreModalidad() : "Sin modalidad");
                row.createCell(19).setCellValue(trabajador.getJornada() != null ? trabajador.getJornada().getNombreJornada() : "Sin jornada");
                row.createCell(20).setCellValue(trabajador.getCargo() != null ? trabajador.getCargo().getNombreCargo() : "Sin cargo");
            }

            // Ajustar el ancho de todas las columnas excepto la del índice 8
            for (int i = 0; i < columnas.length; i++) {
                if (i != 8) {
                    sheet.autoSizeColumn(i);
                }
            }

            // Asignar un ancho específico a la columna con índice 8
            sheet.setColumnWidth(8, 256 * 30); // Ajustar el valor "30" al tamaño deseado

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportarCampanasAExcel(List<Campana> campanas) throws IOException {
        String[] columnas = {"ID", "Campana"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Campanas");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de las campañas
            int rowIdx = 1;
            for (Campana campana : campanas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(campana.getIdCampana());
                row.createCell(1).setCellValue(campana.getNombreCampana());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportarCentrosAExcel(List<Centro> centros) throws IOException {
        String[] columnas = {"ID", "Centro"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Centros");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de las campañas
            int rowIdx = 1;
            for (Centro centro : centros) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(centro.getIdCentro());
                row.createCell(1).setCellValue(centro.getNombreCentro());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportarEmpresasAExcel(List<Empresa> empresas) throws IOException {
        String[] columnas = {"ID", "Empresa"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Empresas");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de las campañas
            int rowIdx = 1;
            for (Empresa empresa : empresas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(empresa.getIdEmpresa());
                row.createCell(1).setCellValue(empresa.getNombreEmpresa());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportarUsuariosAExcel(List<Usuario> usuarios) throws IOException {
        String[] columnas = {"ID", "Usuario", "Nombre Completo", "Roles", "Estado"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Usuarios");

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Poblar las filas con los datos de los usuarios
            int rowIdx = 1;
            for (Usuario usuario : usuarios) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(usuario.getIdUsuario());
                row.createCell(1).setCellValue(usuario.getUsername());
                
                String nombreCompleto = usuario.getTrabajador() != null ? 
                    usuario.getTrabajador().getApellidoPaterno() + " " + 
                    usuario.getTrabajador().getApellidoMaterno() + ", " + 
                    usuario.getTrabajador().getNombreCompleto() : 
                    "Sin trabajador asignado";
                row.createCell(2).setCellValue(nombreCompleto);
                
                String roles = usuario.getRoles() != null && !usuario.getRoles().isEmpty() ?
                    usuario.getRoles().stream()
                        .map(Rol::getNombreRol)
                        .collect(Collectors.joining(", ")) :
                    "Sin roles asignados";
                row.createCell(3).setCellValue(roles);
                
                row.createCell(4).setCellValue(usuario.getEstado() == 1 ? "Activo" : "Inactivo");
            }

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}