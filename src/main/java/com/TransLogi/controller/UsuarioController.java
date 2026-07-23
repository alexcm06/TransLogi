/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.controller;

/**
 *
 * @author sebas
 */
import com.TransLogi.domain.Usuario;
import com.TransLogi.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public UsuarioController(UsuarioService usuarioService, MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {

        var usuarios = usuarioService.getUsuarios(true);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("totalUsuarios", usuarios.size());
        return "usuario/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario,
            BindingResult result,
            @RequestParam(required = false) MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "redirect:/usuario/listado";
        }

        try {

            usuarioService.save(usuario, imagenFile);

            redirectAttributes.addFlashAttribute("todoOk",
                    "Usuario guardado correctamente.");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error",
                    e.getMessage());

        }

        return "redirect:/usuario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idUsuario) {

        usuarioService.delete(idUsuario);

        return "redirect:/usuario/listado";
    }

    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable Integer idUsuario,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuario = usuarioService.getUsuario(idUsuario);

        if (usuario.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/usuario/listado";
        }

        model.addAttribute("usuario", usuario.get());

        return "usuario/modifica";
    }
}
