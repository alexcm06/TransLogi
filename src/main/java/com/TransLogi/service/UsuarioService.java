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

    @Transactional
    public void save(Usuario usuario, MultipartFile imagenFile) {

        boolean nuevoUsuario = usuario.getIdUsuario() == null;

        // Validar correo repetido
        Optional<Usuario> usuarioDuplicado
                = usuarioRepository.findByUsernameOrCorreo(null, usuario.getCorreo());

        if (usuarioDuplicado.isPresent()) {

            Usuario encontrado = usuarioDuplicado.get();

            if (nuevoUsuario || !encontrado.getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new DataIntegrityViolationException("El correo ya está registrado.");
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

            // Mantener roles
            usuario.setRoles(existente.getRoles());

            // Mantener imagen
            usuario.setRutaImagen(existente.getRutaImagen());

            // Mantener contraseña
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {

                usuario.setPassword(existente.getPassword());

            } else {

                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            }

        }

        usuario = usuarioRepository.save(usuario);

        // Si seleccionó imagen nueva
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

        // Rol por defecto para usuarios nuevos
        if (nuevoUsuario) {

            Rol rol = rolRepository.findById(3)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            usuario.getRoles().add(rol);

            usuarioRepository.save(usuario);

        }

    }

    @Transactional
    public void delete(Integer idUsuario) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!usuarioRepository.existsById(idUsuario)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException(
                    "El usuario con ID " + idUsuario + " no existe.");
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            // Excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException(
                    "No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }
}
