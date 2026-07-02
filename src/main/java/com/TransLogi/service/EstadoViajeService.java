package com.TransLogi.service;

import com.TransLogi.domain.EstadoViaje;
import com.TransLogi.repository.EstadoViajeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadoViajeService {

    private final EstadoViajeRepository estadoViajeRepository;

    public EstadoViajeService(EstadoViajeRepository estadoViajeRepository) {
        this.estadoViajeRepository = estadoViajeRepository;
    }

    @Transactional(readOnly = true)
    public List<EstadoViaje> getEstados() {
        return estadoViajeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EstadoViaje> getEstado(Integer idEstado) {
        return estadoViajeRepository.findById(idEstado);
    }

    @Transactional
    public void save(EstadoViaje estadoViaje) {
        estadoViajeRepository.save(estadoViaje);
    }
}