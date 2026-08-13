package com.TransLogi.repository;

import com.TransLogi.domain.Conductor;
import com.TransLogi.domain.Gasto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Integer> {

    List<Gasto> findByViaje_ConductorOrderByFechaDescIdGastoDesc(Conductor conductor);
}
