package com.TransLogi.controller;

import com.TransLogi.service.ConductorService;
import com.TransLogi.service.ViajeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final ViajeService viajeService;
    private final ConductorService conductorService;

    public IndexController(ViajeService viajeService, ConductorService conductorService) {
        this.viajeService = viajeService;
        this.conductorService = conductorService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("viajesProgramados", viajeService.totalViajesProgramados());
        model.addAttribute("viajesProceso", viajeService.totalViajesProceso());
        model.addAttribute("viajesFinalizados", viajeService.totalViajesFinalizados());
        model.addAttribute("conductoresActivos", conductorService.totalConductoresActivos());
        model.addAttribute("ultimosViajes", viajeService.getUltimosViajes());
        return "index";
    }

}
