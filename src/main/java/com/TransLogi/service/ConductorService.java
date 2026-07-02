package com.TransLogi.service;

import com.TransLogi.domain.Conductor;
import com.TransLogi.repository.ConductorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConductorService {

    private final ConductorRepository conductorRepository;
    public ConductorService(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    @Transactional(readOnly = true)
    public List<Conductor> getConductores(boolean activos) {
        if (activos) {
            return conductorRepository.findByEstadoTrue();
        }
        return conductorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Conductor> getConductor(Integer idConductor) {
        return conductorRepository.findById(idConductor);
    }

    @Transactional
    public void save(Conductor conductor) {
        conductorRepository.save(conductor);
    }

    @Transactional
    public void delete(Integer idConductor) {
        if (!conductorRepository.existsById(idConductor)) {
            throw new IllegalArgumentException("El conductor con ID " + idConductor + " no existe.");
        }
        try {
            conductorRepository.deleteById(idConductor);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el conductor. Tiene datos asociados.", e);
        }
    }
}
