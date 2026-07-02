/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.controller;
/**
 *
 * @author sebas
 */

import com.TransLogi.domain.Ubicacion;
import com.TransLogi.service.UbicacionService;
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
@RequestMapping("/ubicacion")
public class UbicacionController {

    private final UbicacionService ubicacionService;
    private final MessageSource messageSource;

    public UbicacionController(UbicacionService ubicacionService, MessageSource messageSource) {
        this.ubicacionService = ubicacionService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var ubicaciones = ubicacionService.getUbicaciones();
        model.addAttribute("ubicaciones", ubicaciones);
        model.addAttribute("totalUbicaciones",ubicaciones.size());
        return "/ubicacion/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Ubicacion ubicacion,
            RedirectAttributes redirectAttributes) {
        ubicacionService.save(ubicacion);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/ubicacion/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idUbicacion,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            ubicacionService.delete(idUbicacion);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "ubicacion.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "ubicacion.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "ubicacion.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/ubicacion/listado";
    }

    @GetMapping("/modificar/{idUbicacion}")
    public String modificar(@PathVariable("idUbicacion") Integer idUbicacion,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Ubicacion> ubicacionOpt = ubicacionService.getUbicacion(idUbicacion);

        if (ubicacionOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("error", null, Locale.getDefault())
            );
            return "redirect:/ubicacion/listado";
        }

        model.addAttribute("ubicacion", ubicacionOpt.get());
        return "/ubicacion/modifica";
    }
}
