package com.TransLogi.service;

import com.TransLogi.domain.RutaCamino;
import com.TransLogi.repository.RutaCaminoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RutaCaminoService {
    
    private final RutaCaminoRepository rutaRepository;

    public RutaCaminoService(RutaCaminoRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }
    
    @Transactional(readOnly=true)
    public List<RutaCamino> getRutas() {
        return rutaRepository.findAllByOrderByRequiereRolAsc();
    }
    
}