package com.TransLogi.controller;
/**
 *
 * @author sebas
 */
import com.TransLogi.domain.Conductor;
import com.TransLogi.service.ConductorService;
import com.TransLogi.service.ViajeService;
import com.TransLogi.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import java.io.IOException;
import java.time.LocalDate;
import org.springframework.security.core.Authentication; 
import org.springframework.web.multipart.MultipartFile;
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
@RequestMapping("/conductor")
public class ConductorController {

    private final ConductorService conductorService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;
    private final ViajeService viajeService; 
    
    public ConductorController(ConductorService conductorService, UsuarioService usuarioService, MessageSource messageSource, ViajeService viajeService) {
        this.conductorService = conductorService;
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
        this.viajeService = viajeService;
    }
    @GetMapping("/listado")
    public String listado(Model model) {
        var conductores = conductorService.getConductores(false);
        model.addAttribute("conductores", conductores);
        model.addAttribute("totalConductores", conductores.size());
        model.addAttribute("conductor", new Conductor());
        model.addAttribute("usuariosDisponibles", usuarioService.getUsuariosSinConductor());
        model.addAttribute("hoy", LocalDate.now());
        return "/conductor/listado";
    } 

    @PostMapping("/guardar")
    public String guardar(Conductor conductor,
            @RequestParam("fotoFrontal") MultipartFile fotoFrontal,
            @RequestParam("fotoReverso") MultipartFile fotoReverso) {

        conductorService.save(conductor, fotoFrontal, fotoReverso);

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
                    messageSource.getMessage("error", null, Locale.getDefault()));
            return "redirect:/conductor/listado";
        }

        Conductor conductor = conductorOpt.get();

        Integer idUsuario = conductor.getUsuario() != null
                ? conductor.getUsuario().getIdUsuario()
                : null;

        model.addAttribute("usuariosDisponibles",
                usuarioService.getUsuariosDisponibles(idUsuario));

        model.addAttribute("conductor", conductor);
        model.addAttribute("hoy", LocalDate.now());

        return "/conductor/modifica";
    }
    
    @GetMapping("/mis-viajes")
    public String verMisViajes(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        //Obtenemos el username de la persona que inició sesión
        String username = authentication.getName();
        
        //Buscamos el conductor vinculado a ese usuario en la BD
        Conductor conductor = conductorService.getConductorPorUsername(username);
        
        if (conductor == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró un perfil de conductor asociado a tu usuario.");
            return "redirect:/"; // O la ruta principal que prefieras si no tiene perfil
        }
        
        //Buscamos los viajes que le pertenecen exclusivamente a este conductor
        var viajes = viajeService.getViajesPorConductor(conductor);
        
        //andamos los datos a la vista de Thymeleaf
        model.addAttribute("viajes", viajes);
        model.addAttribute("totalViajes", viajes.size());
        model.addAttribute("conductor", conductor);
        
        return "conductor/misViajes"; // Buscará el archivo en templates/conductor/mis_viajes.html
    }
    
}
