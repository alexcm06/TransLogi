package com.TransLogi.repository;

import com.TransLogi.domain.Conductor;
import com.TransLogi.domain.Empresa;
import com.TransLogi.domain.EstadoViaje;
import com.TransLogi.domain.Viaje;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {

    List<Viaje> findByConductor(Conductor conductor);

    List<Viaje> findByEmpresa(Empresa empresa);

    List<Viaje> findByEstadoViaje(EstadoViaje estadoViaje);

    List<Viaje> findByFechaProgramada(LocalDate fechaProgramada);

}