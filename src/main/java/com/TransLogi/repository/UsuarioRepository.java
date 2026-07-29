package com.TransLogi.repository;

import com.TransLogi.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Optional<Usuario> findByUsernameAndActivoTrue(String username);

    public List<Usuario> findByActivoTrue();

    public Optional<Usuario> findByUsername(String username);

    public Optional<Usuario> findByUsernameAndPassword(String username, String Password);

    public Optional<Usuario> findByUsernameOrCorreo(String username, String correo);

    public boolean existsByUsernameOrCorreo(String username, String correo);

    @Query("""
    SELECT DISTINCT u
    FROM Usuario u
    JOIN u.roles r
    WHERE u.activo = true
      AND r.idRol = 3
      AND u.idUsuario NOT IN (
            SELECT c.usuario.idUsuario
            FROM Conductor c
            WHERE c.usuario IS NOT NULL
      )
    """)
    List<Usuario> getUsuariosSinConductor();

    @Query("""
    SELECT DISTINCT u
    FROM Usuario u
    JOIN u.roles r
    WHERE u.activo = true
      AND r.idRol = 3
      AND (
            u.idUsuario = :idUsuarioActual
            OR u.idUsuario NOT IN (
                SELECT c.usuario.idUsuario
                FROM Conductor c
                WHERE c.usuario IS NOT NULL)
      )
    """)
    List<Usuario> getUsuariosDisponibles(Integer idUsuarioActual);

}
