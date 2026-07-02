package com.TransLogi.service;

import com.TransLogi.domain.Ubicacion;
import com.TransLogi.repository.UbicacionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Transactional(readOnly = true)
    public List<Ubicacion> getUbicaciones() {
        return ubicacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Ubicacion> getUbicacion(Integer idUbicacion) {
        return ubicacionRepository.findById(idUbicacion);
    }

    @Transactional
    public void save(Ubicacion ubicacion) {
        ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public void delete(Integer idUbicacion) {
        if (!ubicacionRepository.existsById(idUbicacion)) {
            throw new IllegalArgumentException("La ubicación con ID " + idUbicacion + " no existe.");
        }
        try {
            ubicacionRepository.deleteById(idUbicacion);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la ubicación. Tiene datos asociados.", e);
        }
    }
}