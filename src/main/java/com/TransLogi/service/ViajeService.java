package com.TransLogi.service;

import com.TransLogi.domain.Conductor;
import com.TransLogi.domain.Empresa;
import com.TransLogi.domain.EstadoViaje;
import com.TransLogi.domain.Viaje;
import com.TransLogi.repository.ViajeRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ViajeService {

    private final ViajeRepository viajeRepository;

    public ViajeService(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    @Transactional(readOnly = true)
    public List<Viaje> getViajes() {
        return viajeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Viaje> getViaje(Integer idViaje) {
        return viajeRepository.findById(idViaje);
    }

    @Transactional(readOnly = true)
    public List<Viaje> getViajesPorConductor(Conductor conductor) {
        return viajeRepository.findByConductor(conductor);
    }

    @Transactional(readOnly = true)
    public List<Viaje> getViajesPorEmpresa(Empresa empresa) {
        return viajeRepository.findByEmpresa(empresa);
    }

    @Transactional(readOnly = true)
    public List<Viaje> getViajesPorEstado(EstadoViaje estadoViaje) {
        return viajeRepository.findByEstadoViaje(estadoViaje);
    }

    @Transactional(readOnly = true)
    public List<Viaje> getViajesPorFecha(LocalDate fecha) {
        return viajeRepository.findByFechaProgramada(fecha);
    }

    @Transactional
    public void save(Viaje viaje) {
        viajeRepository.save(viaje);
    }

    @Transactional
    public void delete(Integer idViaje) {

        if (!viajeRepository.existsById(idViaje)) {
            throw new IllegalArgumentException("El viaje con ID " + idViaje + " no existe.");
        }

        try {
            viajeRepository.deleteById(idViaje);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el viaje. Tiene datos asociados.", e);
        }
    }
}
