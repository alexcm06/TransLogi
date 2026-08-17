package com.TransLogi.service;

import com.TransLogi.domain.Viaje;
import com.TransLogi.service.ReporteExcelService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

@Service
public class ReporteExcelServiceImpl implements ReporteExcelService {

    @Override
    public ByteArrayInputStream exportarExcel(List<Viaje> viajes) {

        try (
                Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte Viajes");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 8));

            //===========================
            // ESTILOS
            //===========================
            Font tituloPrincipal = workbook.createFont();
            tituloPrincipal.setBold(true);
            tituloPrincipal.setFontHeightInPoints((short) 18);
            tituloPrincipal.setColor(IndexedColors.DARK_BLUE.getIndex());

            CellStyle estiloTitulo = workbook.createCellStyle();
            estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
            estiloTitulo.setVerticalAlignment(VerticalAlignment.CENTER);
            estiloTitulo.setFont(tituloPrincipal);

            Font subtitulo = workbook.createFont();
            subtitulo.setBold(true);
            subtitulo.setFontHeightInPoints((short) 12);

            CellStyle estiloSubtitulo = workbook.createCellStyle();
            estiloSubtitulo.setFont(subtitulo);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            int fila = 0;

            //===========================
            // TITULO
            //===========================
            Row titulo = sheet.createRow(fila++);
            titulo.createCell(0).setCellValue("TRANSLOGI");
            titulo.getCell(0).setCellStyle(estiloTitulo);

            Row subtituloRow = sheet.createRow(fila++);
            subtituloRow.createCell(0).setCellValue("Sistema de Gestión de Transporte");

            fila++;

            Row nombreReporte = sheet.createRow(fila++);
            nombreReporte.createCell(0).setCellValue("REPORTE DE VIAJES");
            nombreReporte.getCell(0).setCellStyle(estiloSubtitulo);

            Row fecha = sheet.createRow(fila++);
            fecha.createCell(0).setCellValue(
                    "Generado el: "
                    + LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            fila += 2;

            //===========================
            // RESUMEN
            //===========================
            // Cuenta viajes por estado para mostrar un bloque de totales.
            long programados = viajes.stream()
                    .filter(v -> v.getEstadoViaje().getNombreEstado().equalsIgnoreCase("Programado"))
                    .count();

            long proceso = viajes.stream()
                    .filter(v -> v.getEstadoViaje().getNombreEstado().equalsIgnoreCase("En proceso"))
                    .count();

            long finalizados = viajes.stream()
                    .filter(v -> v.getEstadoViaje().getNombreEstado().equalsIgnoreCase("Finalizado"))
                    .count();

            long cancelados = viajes.stream()
                    .filter(v -> v.getEstadoViaje().getNombreEstado().equalsIgnoreCase("Cancelado"))
                    .count();

            Row resumen = sheet.createRow(fila++);
            resumen.createCell(0).setCellValue("RESUMEN");
            resumen.getCell(0).setCellStyle(estiloSubtitulo);

            sheet.createRow(fila++).createCell(0)
                    .setCellValue("Total de viajes: " + viajes.size());

            sheet.createRow(fila++).createCell(0)
                    .setCellValue("Programados: " + programados);

            sheet.createRow(fila++).createCell(0)
                    .setCellValue("En proceso: " + proceso);

            sheet.createRow(fila++).createCell(0)
                    .setCellValue("Finalizados: " + finalizados);

            sheet.createRow(fila++).createCell(0)
                    .setCellValue("Cancelados: " + cancelados);

            fila += 2;

            //===========================
            // TABLA
            //===========================
            String[] columnas = {
                "ID",
                "Fecha",
                "Hora",
                "Empresa",
                "Conductor",
                "Origen",
                "Destino",
                "Estado",
                "Pasajeros"
            };

            Row header = sheet.createRow(fila++);

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

            // Cada viaje se escribe en una fila con el mismo orden del encabezado.
            for (Viaje v : viajes) {

                Row row = sheet.createRow(fila++);

                Cell c0 = row.createCell(0);
                c0.setCellValue(v.getIdViaje());
                c0.setCellStyle(dataStyle);

                Cell c1 = row.createCell(1);
                c1.setCellValue(v.getFechaProgramada().toString());
                c1.setCellStyle(dataStyle);

                Cell c2 = row.createCell(2);
                c2.setCellValue(v.getHoraProgramada().format(formatoHora));
                c2.setCellStyle(dataStyle);

                Cell c3 = row.createCell(3);
                c3.setCellValue(v.getEmpresa().getNombre());
                c3.setCellStyle(dataStyle);

                Cell c4 = row.createCell(4);
                c4.setCellValue(v.getConductor().getNombre());
                c4.setCellStyle(dataStyle);

                Cell c5 = row.createCell(5);
                c5.setCellValue(v.getOrigen().getNombre());
                c5.setCellStyle(dataStyle);

                Cell c6 = row.createCell(6);
                c6.setCellValue(v.getDestino().getNombre());
                c6.setCellStyle(dataStyle);

                Cell c7 = row.createCell(7);
                c7.setCellValue(v.getEstadoViaje().getNombreEstado());
                c7.setCellStyle(dataStyle);

                Cell c8 = row.createCell(8);
                c8.setCellValue(v.getCantidadPasajeros());
                c8.setCellStyle(dataStyle);
            }

            fila++;

            Row footer = sheet.createRow(fila);
            footer.createCell(0).setCellValue("Generado automáticamente por TransLogi.");

            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el Excel.", e);
        }
    }
}
