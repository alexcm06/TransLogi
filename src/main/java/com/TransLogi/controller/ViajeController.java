/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.controller;
/**
 *
 * @author sebas
 */

import com.TransLogi.domain.Viaje;
import com.TransLogi.service.ViajeService;
import com.TransLogi.service.EmpresaService;
import com.TransLogi.service.ConductorService;
import com.TransLogi.service.UbicacionService;
import com.TransLogi.service.EstadoViajeService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/viaje")
public class ViajeController {

    private final ViajeService viajeService;
    private final EmpresaService empresaService;
    private final ConductorService conductorService;
    private final UbicacionService ubicacionService;
    private final EstadoViajeService estadoViajeService;
    private final MessageSource messageSource;

    public ViajeController(ViajeService viajeService,
            EmpresaService empresaService,
            ConductorService conductorService,
            UbicacionService ubicacionService,
            EstadoViajeService estadoViajeService,
            MessageSource messageSource) {
        this.viajeService = viajeService;
        this.empresaService = empresaService;
        this.conductorService = conductorService;
        this.ubicacionService = ubicacionService;
        this.estadoViajeService = estadoViajeService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var viajes = viajeService.getViajes();
        model.addAttribute("viajes", viajes);
        model.addAttribute("totalViajes", viajes.size());
        cargarListasParaFormulario(model);
        return "/viaje/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Viaje viaje,
            RedirectAttributes redirectAttributes) {
        viajeService.save(viaje);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/viaje/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idViaje,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            viajeService.delete(idViaje);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "viaje.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "viaje.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "viaje.error03";
        }
        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/viaje/listado";
    }

    @GetMapping("/modificar/{idViaje}")
    public String modificar(@PathVariable("idViaje") Integer idViaje,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Viaje> viajeOpt = viajeService.getViaje(idViaje);
        if (viajeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("error", null, Locale.getDefault())
            );
            return "redirect:/viaje/listado";
        }
        model.addAttribute("viaje", viajeOpt.get());
        cargarListasParaFormulario(model);
        return "/viaje/modifica";
    }

    private void cargarListasParaFormulario(Model model) {
        model.addAttribute("empresas", empresaService.getEmpresas(true));
        model.addAttribute("conductores", conductorService.getConductores(true));
        model.addAttribute("ubicaciones", ubicacionService.getUbicaciones());
        model.addAttribute("estados", estadoViajeService.getEstados());
    }
}