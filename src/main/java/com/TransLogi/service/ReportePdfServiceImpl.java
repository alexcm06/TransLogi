/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.service;

import com.TransLogi.domain.Viaje;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
/**
 *
 * @author sebas
 */
@Service
public class ReportePdfServiceImpl implements ReportePdfService {

    @Override
    public ByteArrayInputStream exportarPdf(List<Viaje> viajes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Titulo principal del reporte.
            document.add(new Paragraph("TRANSLOGI - Reporte de Viajes")
                    .setBold()
                    .setFontSize(18));

            // Tabla con las mismas columnas usadas en el Excel.
            Table table = new Table(UnitValue.createPercentArray(new float[]{8, 12, 10, 15, 15, 12, 12, 10, 6}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Encabezados visibles del reporte.
            String[] columnas = {"ID", "Fecha", "Hora", "Empresa", "Conductor", "Origen", "Destino", "Estado", "Pasajeros"};
            for (String col : columnas) {
                table.addHeaderCell(col);
            }

            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

            // Filas generadas desde los viajes filtrados.
            for (Viaje v : viajes) {
                table.addCell(v.getIdViaje() != null ? String.valueOf(v.getIdViaje()) : "");
                table.addCell(v.getFechaProgramada() != null ? v.getFechaProgramada().toString() : "");
                table.addCell(v.getHoraProgramada() != null ? v.getHoraProgramada().format(formatoHora) : "");
                table.addCell(v.getEmpresa() != null ? v.getEmpresa().getNombre() : "");
                table.addCell(v.getConductor() != null ? v.getConductor().getNombre() : "");
                table.addCell(v.getOrigen() != null ? v.getOrigen().getNombre() : "");
                table.addCell(v.getDestino() != null ? v.getDestino().getNombre() : "");
                table.addCell(v.getEstadoViaje() != null ? v.getEstadoViaje().getNombreEstado() : "");
                table.addCell(String.valueOf(v.getCantidadPasajeros()));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
