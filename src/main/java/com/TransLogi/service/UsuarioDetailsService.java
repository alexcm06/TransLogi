package com.TransLogi.service;

import com.TransLogi.domain.Usuario;
import com.TransLogi.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final HttpSession session;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository, HttpSession session) {
        this.usuarioRepository = usuarioRepository;
        this.session = session;
    }

    // Busca el usuario activo y prepara sus datos para Spring Security.
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Solo usuarios activos pueden autenticarse.
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // La imagen queda disponible para el sidebar.
        session.removeAttribute("imagenUsuario");
        session.setAttribute("imagenUsuario", usuario.getRutaImagen());

        // Spring Security requiere el prefijo ROLE_ para validar permisos.
        var roles = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol()))
                .collect(Collectors.toSet());

        // Retorna username, password encriptado y roles cargados desde BD.
        return new User(usuario.getUsername(), usuario.getPassword(), roles);
    }
}
