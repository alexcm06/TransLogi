package com.TransLogi.repository;

import com.TransLogi.domain.Conductor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Integer> {

    List<Conductor> findByEstadoTrue();

    Optional<Conductor> findByNumeroLicencia(String numeroLicencia);

}
