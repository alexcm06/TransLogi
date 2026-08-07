/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //Método puente para redirigir según el rol tras el login exitoso
    @GetMapping("/home")
    public String redirigirSegunRol(Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_Administrador"));

        boolean isConductor = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_Conductor"));

        if (isAdmin) {
            return "redirect:/viaje/listado";
        } else if (isConductor) {
            return "redirect:/conductor/mis-viajes";
        } else {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/acceso_denegado")
    public String accesoDenegado() {
        return "acceso_denegado";
    }
}
