/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.controller;
/**
 *
 * @author sebas
 */
import com.TransLogi.domain.EstadoViaje;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estadoViaje")
public class EstadoViajeController {
    
    private final EstadoViajeService estadoViajeService;
    private final MessageSource messageSource;

    public EstadoViajeController(EstadoViajeService estadoViajeService, MessageSource messageSource) {
        this.estadoViajeService = estadoViajeService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var estadoViajes = estadoViajeService.getEstados();
        model.addAttribute("estadoViajes", estadoViajes);
        model.addAttribute("totalestadoViajes", estadoViajes.size());
        return "/estadoViaje/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid EstadoViaje estadoViaje,
            RedirectAttributes redirectAttributes) {
        //Pasamos el objeto al método save
        estadoViajeService.save(estadoViaje);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/estadoViaje/listado";
    }

    @GetMapping("/modificar/{idEstado}")
    public String modificar(@PathVariable("idEstado") Integer idEstado,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<EstadoViaje> estadoOpt = estadoViajeService.getEstado(idEstado);

        if (estadoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("error", null, Locale.getDefault())
            );
            return "redirect:/estadoViaje/listado";
        }
        model.addAttribute("estadoViaje", estadoOpt.get());
        return "/estadoViaje/modifica";
    }
}