package com.TransLogi.service;

import com.TransLogi.domain.Rol;
import com.TransLogi.domain.Usuario;
import com.TransLogi.repository.RolRepository;
import com.TransLogi.repository.UsuarioRepository;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            FirebaseStorageService firebaseStorageService,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios(boolean activo) {
        if (activo) {
            return usuarioRepository.findByActivoTrue();
        }
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsernameYPassword(String username,
            String password) {
        return usuarioRepository.findByUsernameAndPassword(username, password);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsernameOCorreo(String username,
            String correo) {
        return usuarioRepository.findByUsernameOrCorreo(username, correo);
    }

    @Transactional(readOnly = true)
    public boolean existeUsuarioPorUsernameOCorreo(String username,
            String correo) {
        return usuarioRepository.existsByUsernameOrCorreo(username, correo);
    }
    
    @Transactional(readOnly = true)
    public List<Rol> getRolesDisponibles() {
        return rolRepository.findAll();
    }
    
    public List<Usuario> getUsuariosSinConductor() {
        return usuarioRepository.getUsuariosSinConductor();
    }
    
    public List<Usuario> getUsuariosDisponibles(Integer idUsuarioActual) {
        return usuarioRepository.getUsuariosDisponibles(idUsuarioActual);
    }
    
    @Transactional
    public void save(Usuario usuario, MultipartFile imagenFile,Integer idRol) {

        boolean nuevoUsuario = usuario.getIdUsuario() == null;

        // Evita usuarios y correos duplicados entre registros.
        Optional<Usuario> usuarioDuplicado =
            usuarioRepository.findByUsernameOrCorreo(usuario.getUsername(), usuario.getCorreo());

        if (usuarioDuplicado.isPresent()) {
            Usuario encontrado = usuarioDuplicado.get();
            if (nuevoUsuario || !encontrado.getIdUsuario().equals(usuario.getIdUsuario())) {
                if (encontrado.getUsername().equalsIgnoreCase(usuario.getUsername())) {
                    throw new DataIntegrityViolationException("El usuario ya esta registrado.");
                }

                if (encontrado.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                    throw new DataIntegrityViolationException("El correo ya esta registrado.");
                }
            }
        }
        if (nuevoUsuario) {
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                throw new IllegalArgumentException("Debe ingresar una contraseña.");
            }
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            Usuario existente = usuarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
            // Conserva roles, imagen y password si el formulario no los cambia.
            usuario.setRoles(existente.getRoles());
            usuario.setRutaImagen(existente.getRutaImagen());
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                usuario.setPassword(existente.getPassword());
            } else {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        usuario = usuarioRepository.save(usuario);

        // Si llega una imagen nueva, se sube y se guarda su URL.
        if (imagenFile != null && !imagenFile.isEmpty()) {

            try {
                String ruta = firebaseStorageService.uploadImage(
                        imagenFile,
                        "usuario",
                        usuario.getIdUsuario());
                usuario.setRutaImagen(ruta);
                usuarioRepository.save(usuario);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen.");
            }

        }
        // Los usuarios nuevos reciben el rol indicado o Conductor por defecto.
        if (nuevoUsuario) {
            Integer idRolFinal = (idRol != null) ? idRol : 3;
            Rol rol = rolRepository.findById(idRolFinal)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            usuario.getRoles().add(rol);
            usuarioRepository.save(usuario);
        }
    }

    @Transactional
    public void delete(Integer idUsuario) {
        // Verifica que exista antes de eliminarlo.
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException(
                    "El usuario con ID " + idUsuario + " no existe.");
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            // Evita borrar usuarios con datos asociados.
            throw new IllegalStateException(
                    "No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }
}
