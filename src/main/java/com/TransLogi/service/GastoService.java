package com.TransLogi.service;

import com.TransLogi.domain.Conductor;
import com.TransLogi.domain.Gasto;
import com.TransLogi.domain.TipoGasto;
import com.TransLogi.repository.GastoRepository;
import com.TransLogi.repository.TipoGastoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private final TipoGastoRepository tipoGastoRepository;

    public GastoService(GastoRepository gastoRepository,
            TipoGastoRepository tipoGastoRepository) {
        this.gastoRepository = gastoRepository;
        this.tipoGastoRepository = tipoGastoRepository;
    }

    @Transactional(readOnly = true)
    public List<TipoGasto> getTiposGasto() {
        return tipoGastoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TipoGasto> getTipoGasto(Integer idTipoGasto) {
        return tipoGastoRepository.findById(idTipoGasto);
    }

    @Transactional(readOnly = true)
    public Optional<Gasto> getGasto(Integer idGasto) {
        return gastoRepository.findById(idGasto);
    }

    @Transactional(readOnly = true)
    public List<Gasto> getGastosPorConductor(Conductor conductor) {
        return gastoRepository.findByViaje_ConductorOrderByFechaDescIdGastoDesc(conductor);
    }

    @Transactional
    public void save(Gasto gasto) {
        gastoRepository.save(gasto);
    }

    @Transactional
    public void delete(Integer idGasto) {
        gastoRepository.deleteById(idGasto);
    }
}
