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
}