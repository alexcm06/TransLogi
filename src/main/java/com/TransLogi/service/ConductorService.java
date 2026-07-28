package com.TransLogi.service;

import com.TransLogi.domain.Conductor;
import com.TransLogi.repository.ConductorRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ConductorService {

    private final ConductorRepository conductorRepository;
    private final FirebaseStorageService firebaseStorageService;

    public ConductorService(ConductorRepository conductorRepository,
            FirebaseStorageService firebaseStorageService) {
        this.conductorRepository = conductorRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Transactional(readOnly = true)
    public List<Conductor> getConductores(boolean activos) {
        if (activos) {
            return conductorRepository.findByActivoTrue();
        }
        return conductorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Conductor> getConductor(Integer idConductor) {
        return conductorRepository.findById(idConductor);
    }

    @Transactional(readOnly = true)
    public long totalConductoresActivos() {
        return conductorRepository.totalConductoresActivo(true);
    }

    @Transactional
    public void save(Conductor conductor,
            MultipartFile fotoFrontal,
            MultipartFile fotoReverso) {

        // Si es edición, recuperar las imágenes actuales
        if (conductor.getIdConductor() != null) {

            Conductor existente = conductorRepository.findById(conductor.getIdConductor())
                    .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));

            if (fotoFrontal == null || fotoFrontal.isEmpty()) {
                conductor.setFotoLicenciaFrente(existente.getFotoLicenciaFrente());
            }

            if (fotoReverso == null || fotoReverso.isEmpty()) {
                conductor.setFotoLicenciaReverso(existente.getFotoLicenciaReverso());
            }
        }

        // Guardar primero para obtener el ID si es nuevo
        conductor = conductorRepository.save(conductor);

        try {

            if (fotoFrontal != null && !fotoFrontal.isEmpty()) {

                String urlFrente = firebaseStorageService.uploadImage(
                        fotoFrontal,
                        "licencia_frente",
                        conductor.getIdConductor());

                conductor.setFotoLicenciaFrente(urlFrente);
            }

            if (fotoReverso != null && !fotoReverso.isEmpty()) {

                String urlReverso = firebaseStorageService.uploadImage(
                        fotoReverso,
                        "licencia_reverso",
                        conductor.getIdConductor());

                conductor.setFotoLicenciaReverso(urlReverso);
            }

            conductorRepository.save(conductor);

        } catch (IOException e) {
            throw new RuntimeException("Error al subir las imágenes de la licencia.", e);
        }
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
