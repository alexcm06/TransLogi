package com.TransLogi.service;

import com.TransLogi.domain.Empresa;
import com.TransLogi.repository.EmpresaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public List<Empresa> getEmpresas(boolean activos) {
        if (activos) {
            return empresaRepository.findByEstadoTrue();
        }
        return empresaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Empresa> getEmpresa(Integer idEmpresa) {
        return empresaRepository.findById(idEmpresa);
    }

    @Transactional
    public void save(Empresa empresa) {
        empresaRepository.save(empresa);
    }

    @Transactional
    public void delete(Integer idEmpresa) {

        if (!empresaRepository.existsById(idEmpresa)) {
            throw new IllegalArgumentException("La empresa con ID " + idEmpresa + " no existe.");
        }

        try {
            empresaRepository.deleteById(idEmpresa);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la empresa. Tiene datos asociados.", e);
        }
    }
}
