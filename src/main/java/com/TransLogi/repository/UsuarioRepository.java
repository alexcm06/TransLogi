package com.TransLogi.repository;

import com.TransLogi.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    public Optional<Usuario> findByUsernameAndActivoTrue(String username);

//    List<Usuario> findByEstadoTrue();
//
//    Optional<Usuario> findByCorreo(String correo);

}
