package com.customerpriority.sig.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.customerpriority.sig.model.Campana;
import com.customerpriority.sig.model.Cargo;
import com.customerpriority.sig.model.Horario;

@Service
public class ExcelExportService {
    public ByteArrayInputStream exportarCampanasAExcel(List<Campana> campanas) throws IOException {
        String[] columnas = {"ID", "Segmento", "Nombre Campaña", "Gestión", "Estado"};

        // Crear un nuevo workbook y hoja
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Campañas");

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
                row.createCell(1).setCellValue(campana.getSegmento());
                row.createCell(2).setCellValue(campana.getNombreCampana());
                row.createCell(3).setCellValue(campana.getGestion());
                row.createCell(4).setCellValue(campana.getEstado());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    public ByteArrayInputStream exportarCargosAExcel(List<Cargo> cargos) throws IOException {
        String[] columnas = {"ID", "Cargo"};

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
}
