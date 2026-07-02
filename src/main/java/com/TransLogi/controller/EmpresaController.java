/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.controller;
/**
 *
 * @author sebas
 */
import com.TransLogi.domain.Empresa;
import com.TransLogi.service.EmpresaService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
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
@RequestMapping("/empresa")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final MessageSource messageSource;

    public EmpresaController(EmpresaService empresaService, MessageSource messageSource) {
        this.empresaService = empresaService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var empresas = empresaService.getEmpresas(false);
        model.addAttribute("empresas", empresas);
        model.addAttribute("totalEmpresas", empresas.size());
        return "/empresa/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Empresa empresa,
            RedirectAttributes redirectAttributes) {
        empresaService.save(empresa);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/empresa/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idEmpresa,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            empresaService.delete(idEmpresa);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "empresa.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "empresa.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "empresa.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/empresa/listado";
    }

    @GetMapping("/modificar/{idEmpresa}")
    public String modificar(@PathVariable("idEmpresa") Integer idEmpresa,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Empresa> empresaOpt = empresaService.getEmpresa(idEmpresa);

        if (empresaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("error", null, Locale.getDefault())
            );
            return "redirect:/empresa/listado";
        }

        model.addAttribute("empresa", empresaOpt.get());
        return "/empresa/modifica";
    }
}
