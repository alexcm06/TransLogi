package com.TransLogi.repository;

import com.TransLogi.domain.Conductor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Integer> {

    List<Conductor> findByEstadoTrue();

    Optional<Conductor> findByNumeroLicencia(String numeroLicencia);
    
    // Consulta JPQL que obtiene la cantidad de conductores activos
    @Query(value = "SELECT COUNT(c) FROM Conductor c WHERE c.estado = :estado")
    public long totalConductoresEstado(@Param("estado") boolean estado);
}
