package com.TransLogi.controller;
/**
 *
 * @author sebas
 */

import com.TransLogi.domain.Conductor;
import com.TransLogi.service.ArchivoService;
import com.TransLogi.service.ConductorService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.MessageSource; // Traer textos desde el archivo messages.properties, en vez de escribirlos directo en el código Java
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/conductor")
public class ConductorController {

    private final ConductorService conductorService;
    private final ArchivoService archivoService;
    private final MessageSource messageSource;

    public ConductorController(ConductorService conductorService, ArchivoService archivoService, MessageSource messageSource) {
        this.conductorService = conductorService;
        this.archivoService = archivoService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var conductores = conductorService.getConductores(false);
        model.addAttribute("conductores", conductores);
        model.addAttribute("totalConductores", conductores.size());
        model.addAttribute("conductor", new Conductor());
        return "/conductor/listado";
    }

    @PostMapping("/guardar")
    public String guardar(
        @Valid Conductor conductor,
        @RequestParam("imagen") MultipartFile imagen,
        RedirectAttributes redirectAttributes) {

    try {

        if (!imagen.isEmpty()) {
            String nombreArchivo =
                    archivoService.guardarImagen(imagen);
            conductor.setFotoLicencia(nombreArchivo);
        }
        conductorService.save(conductor);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage(
                        "mensaje.actualizado",
                        null,
                        Locale.getDefault())
        );

    } catch (IOException e) {
        redirectAttributes.addFlashAttribute(
                "error",
                "No fue posible guardar la imagen."
        );
    }

    return "redirect:/conductor/listado";
}

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idConductor,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            conductorService.delete(idConductor);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "conductor.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "conductor.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "conductor.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/conductor/listado";
    }

    @GetMapping("/modificar/{idConductor}")
    public String modificar(@PathVariable("idConductor") Integer idConductor,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Conductor> conductorOpt = conductorService.getConductor(idConductor);

        if (conductorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("error", null, Locale.getDefault())
            );
            return "redirect:/conductor/listado";
        }

        model.addAttribute("conductor", conductorOpt.get());
        return "/conductor/modifica";
    }
}
