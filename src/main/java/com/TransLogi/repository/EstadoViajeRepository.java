package com.TransLogi.repository;

import com.TransLogi.domain.EstadoViaje;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoViajeRepository extends JpaRepository<EstadoViaje, Integer> {

    Optional<EstadoViaje> findByNombreEstado(String nombreEstado);

}
