/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.controller;

import com.TransLogi.domain.Viaje;
import com.TransLogi.service.ConductorService;
import com.TransLogi.service.EmpresaService;
import com.TransLogi.service.EstadoViajeService;
import com.TransLogi.service.ReporteExcelService;
import com.TransLogi.service.ViajeService;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ViajeService viajeService;
    private final EmpresaService empresaService;
    private final ConductorService conductorService;
    private final EstadoViajeService estadoViajeService;
    private final ReporteExcelService reporteExcelService;

    @GetMapping("/listado")
    public String listado(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) Integer empresa,
            @RequestParam(required = false) Integer conductor,
            @RequestParam(required = false) Integer estado,
            Model model) {

        model.addAttribute("empresas",
                empresaService.getEmpresas(true));

        model.addAttribute("conductores",
                conductorService.getConductores(true));

        model.addAttribute("estados",
                estadoViajeService.getEstados());

        List<Viaje> viajes = viajeService.obtenerReporte(
                fechaInicio,
                fechaFin,
                empresa,
                conductor,
                estado);

        model.addAttribute("viajes", viajes);

        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("empresa", empresa);
        model.addAttribute("conductor", conductor);
        model.addAttribute("estado", estado);

        return "reporte/listado";
    }
    
    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> exportarExcel(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) Integer empresa,
            @RequestParam(required = false) Integer conductor,
            @RequestParam(required = false) Integer estado) {

        List<Viaje> viajes = viajeService.obtenerReporte(
                fechaInicio,
                fechaFin,
                empresa,
                conductor,
                estado);

        ByteArrayInputStream excel
                = reporteExcelService.exportarExcel(viajes);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=Reporte_Viajes.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(excel));
    }

}
