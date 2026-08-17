package com.TransLogi.controller;

import java.util.Locale;
import com.TransLogi.domain.Conductor;
import com.TransLogi.domain.Gasto;
import com.TransLogi.domain.Viaje;
import com.TransLogi.service.ConductorService;
import com.TransLogi.service.EstadoViajeService;
import com.TransLogi.service.GastoService;
import com.TransLogi.service.UsuarioService;
import com.TransLogi.service.ViajeService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/conductor")
public class ConductorController {

    private final ConductorService conductorService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;
    private final ViajeService viajeService;
    private final EstadoViajeService estadoViajeService;
    private final GastoService gastoService;

    public ConductorController(ConductorService conductorService,
            UsuarioService usuarioService,
            MessageSource messageSource,
            ViajeService viajeService,
            EstadoViajeService estadoViajeService,
            GastoService gastoService) {
        this.conductorService = conductorService;
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
        this.viajeService = viajeService;
        this.estadoViajeService = estadoViajeService;
        this.gastoService = gastoService;
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
    public String verMisViajes(Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Muestra solo los viajes asignados al conductor autenticado.
        String username = authentication.getName();
        Conductor conductor = conductorService.getConductorPorUsername(username);

        if (conductor == null) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("conductor.noPerfil", null, Locale.getDefault()));
            return "redirect:/";
        }

        var viajes = viajeService.getViajesPorConductor(conductor);
        var viajesConGastos = viajes.stream()
                .filter(viaje -> viaje.getEstadoViaje() != null
                && !viaje.getEstadoViaje().getNombreEstado().equalsIgnoreCase("Programado"))
                .toList();

        model.addAttribute("viajes", viajes);
        model.addAttribute("viajesConGastos", viajesConGastos);
        model.addAttribute("gastos", gastoService.getGastosPorConductor(conductor));
        model.addAttribute("tiposGasto", gastoService.getTiposGasto());
        model.addAttribute("fechaGastoSugerida", LocalDate.now());
        model.addAttribute("totalViajes", viajes.size());
        model.addAttribute("conductor", conductor);

        return "conductor/misViajes";
    }

    @GetMapping("/viaje/detalle/{idViaje}")
    public String detalleViaje(@PathVariable Integer idViaje,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Valida que el viaje pertenezca al conductor antes de mostrarlo.
        String username = authentication.getName();
        Conductor conductor = conductorService.getConductorPorUsername(username);

        Optional<Viaje> viajeOpt = viajeService.getViaje(idViaje);

        if (viajeOpt.isEmpty() || conductor == null
                || !viajeOpt.get().getConductor().getIdConductor().equals(conductor.getIdConductor())) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.noEncontrado", null, Locale.getDefault()));
            return "redirect:/conductor/mis-viajes";
        }

        Viaje viaje = viajeOpt.get();
        LocalDateTime fechaHoraProgramada = LocalDateTime.of(
                viaje.getFechaProgramada(),
                viaje.getHoraProgramada());
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaHoraInicioSugerida =
                viaje.getFechaHoraInicio() != null ? viaje.getFechaHoraInicio() : ahora;
        DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // El inicio se habilita desde 30 minutos antes de la hora programada.
        model.addAttribute("viaje", viaje);
        model.addAttribute("puedeIniciar",
                !ahora.isBefore(fechaHoraProgramada.minusMinutes(30)));
        model.addAttribute("fechaHoraInicioInput",
                fechaHoraInicioSugerida.format(formatoFechaHora));
        model.addAttribute("fechaHoraFinInput",
                ahora.format(formatoFechaHora));

        return "conductor/detalleViaje";
    }

    @PostMapping("/viaje/iniciar")
    public String iniciarViaje(@RequestParam Integer idViaje,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        Conductor conductor = conductorService.getConductorPorUsername(username);

        Optional<Viaje> viajeOpt = viajeService.getViaje(idViaje);

        if (viajeOpt.isEmpty() || conductor == null
                || !viajeOpt.get().getConductor().getIdConductor().equals(conductor.getIdConductor())) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.noEncontrado", null, Locale.getDefault()));
            return "redirect:/conductor/mis-viajes";
        }

        Viaje viaje = viajeOpt.get();
        LocalDateTime fechaHoraProgramada = LocalDateTime.of(
                viaje.getFechaProgramada(),
                viaje.getHoraProgramada());
        LocalDateTime ahora = LocalDateTime.now();

        // Evita iniciar viajes antes de la ventana permitida.
        if (ahora.isBefore(fechaHoraProgramada.minusMinutes(30))) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.inicioNoDisponible", null, Locale.getDefault()));
            return "redirect:/conductor/viaje/detalle/" + idViaje;
        }

        if (!viaje.getEstadoViaje().getNombreEstado().equalsIgnoreCase("Programado")) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.soloProgramados", null, Locale.getDefault()));
            return "redirect:/conductor/viaje/detalle/" + idViaje;
        }

        viaje.setEstadoViaje(estadoViajeService.getEstadoEnProceso());
        viaje.setFechaHoraInicio(ahora);
        viajeService.save(viaje);

        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("viaje.iniciado", null, Locale.getDefault()));

        return "redirect:/conductor/viaje/detalle/" + idViaje;
    }

    @PostMapping("/viaje/finalizar")
    public String finalizarViaje(@RequestParam Integer idViaje,
            @RequestParam BigDecimal kilometrosRecorridos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHoraInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHoraFin,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        Conductor conductor = conductorService.getConductorPorUsername(username);

        Optional<Viaje> viajeOpt = viajeService.getViaje(idViaje);

        if (viajeOpt.isEmpty() || conductor == null
                || !viajeOpt.get().getConductor().getIdConductor().equals(conductor.getIdConductor())) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.noEncontrado", null, Locale.getDefault()));
            return "redirect:/conductor/mis-viajes";
        }

        Viaje viaje = viajeOpt.get();

        // Solo un viaje en proceso puede pasar a finalizado.
        if (!viaje.getEstadoViaje().getNombreEstado().equalsIgnoreCase("En proceso")) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.soloProceso", null, Locale.getDefault()));
            return "redirect:/conductor/viaje/detalle/" + idViaje;
        }

        if (fechaHoraFin.isBefore(fechaHoraInicio)) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.fechaFinAnterior", null, Locale.getDefault()));
            return "redirect:/conductor/viaje/detalle/" + idViaje;
        }

        if (kilometrosRecorridos.compareTo(BigDecimal.ZERO) < 0) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.kilometrosNegativos", null, Locale.getDefault()));
            return "redirect:/conductor/viaje/detalle/" + idViaje;
        }

        // Guarda los datos reales del cierre antes de cambiar el estado.
        viaje.setKilometrosRecorridos(kilometrosRecorridos);
        viaje.setFechaHoraInicio(fechaHoraInicio);
        viaje.setFechaHoraFin(fechaHoraFin);
        viaje.setEstadoViaje(estadoViajeService.getEstadoFinalizado());
        viajeService.save(viaje);

        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("viaje.finalizadoOk", null, Locale.getDefault()));

        return "redirect:/conductor/viaje/detalle/" + idViaje;
    }

    @PostMapping("/gasto/guardar")
    public String guardarGasto(@RequestParam Integer idViaje,
            @RequestParam Integer idTipoGasto,
            @RequestParam BigDecimal monto,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String descripcion,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        Conductor conductor = conductorService.getConductorPorUsername(username);

        Optional<Viaje> viajeOpt = viajeService.getViaje(idViaje);

        if (viajeOpt.isEmpty() || conductor == null
                || !viajeOpt.get().getConductor().getIdConductor().equals(conductor.getIdConductor())) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("viaje.noEncontrado", null, Locale.getDefault()));
            return "redirect:/conductor/mis-viajes";
        }

        if (monto.compareTo(BigDecimal.ZERO) < 0) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("gasto.montoNegativo", null, Locale.getDefault()));
            return "redirect:/conductor/mis-viajes";
        }

        Viaje viaje = viajeOpt.get();
        // Los gastos se registran solo en viajes iniciados o finalizados.
        if (viaje.getEstadoViaje() == null
                || viaje.getEstadoViaje().getNombreEstado().equalsIgnoreCase("Programado")) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("gasto.viajeNoDisponible", null, Locale.getDefault()));
            return "redirect:/conductor/mis-viajes";
        }

        var tipoGastoOpt = gastoService.getTipoGasto(idTipoGasto);
        if (tipoGastoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("gasto.tipoInvalido", null, Locale.getDefault()));
            return "redirect:/conductor/mis-viajes";
        }

        Gasto gasto = new Gasto();
        gasto.setDescripcion(descripcion);
        gasto.setMonto(monto);
        gasto.setFecha(fecha);
        gasto.setViaje(viaje);
        gasto.setTipoGasto(tipoGastoOpt.get());
        gastoService.save(gasto);

        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("gasto.guardado", null, Locale.getDefault()));

        return "redirect:/conductor/mis-viajes";
    }

    @PostMapping("/gasto/eliminar")
    public String eliminarGasto(@RequestParam Integer idGasto,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        Conductor conductor = conductorService.getConductorPorUsername(username);

        var gastoOpt = gastoService.getGasto(idGasto);

        if (gastoOpt.isEmpty() || conductor == null
                || !gastoOpt.get().getViaje().getConductor().getIdConductor().equals(conductor.getIdConductor())) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("gasto.noEncontrado", null, Locale.getDefault()));
            return "redirect:/conductor/mis-viajes";
        }

        gastoService.delete(idGasto);

        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("gasto.eliminado", null, Locale.getDefault()));

        return "redirect:/conductor/mis-viajes";
    }
}
