package com.TransLogi.repository;

import com.TransLogi.domain.Conductor;
import com.TransLogi.domain.Empresa;
import com.TransLogi.domain.EstadoViaje;
import com.TransLogi.domain.Viaje;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {

    List<Viaje> findByConductor(Conductor conductor);

    List<Viaje> findByEmpresa(Empresa empresa);

    List<Viaje> findByEstadoViaje(EstadoViaje estadoViaje);

    List<Viaje> findByFechaProgramada(LocalDate fechaProgramada);

    public List<Viaje> findTop5ByOrderByFechaProgramadaDesc();

    // Consulta JPQL que obtiene la cantidad de viajes por estado
    @Query(value = "SELECT COUNT(v) FROM Viaje v WHERE v.estadoViaje.nombreEstado = :estado")
    public long totalViajesPorEstado(@Param("estado") String estado);

    @Query("""
        SELECT v
        FROM Viaje v
        WHERE
            (:fechaInicio IS NULL OR v.fechaProgramada >= :fechaInicio)
        AND (:fechaFin IS NULL OR v.fechaProgramada <= :fechaFin)
        AND (:empresa IS NULL OR v.empresa.idEmpresa = :empresa)
        AND (:conductor IS NULL OR v.conductor.idConductor = :conductor)
        AND (:estado IS NULL OR v.estadoViaje.idEstadoViaje = :estado)
        ORDER BY v.fechaProgramada DESC, v.horaProgramada DESC
        """)
    List<Viaje> obtenerReporte(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("empresa") Integer empresa,
            @Param("conductor") Integer conductor,
            @Param("estado") Integer estado);
}
