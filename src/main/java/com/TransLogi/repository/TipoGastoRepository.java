package com.TransLogi.repository;

import com.TransLogi.domain.TipoGasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoGastoRepository extends JpaRepository<TipoGasto, Integer> {

}
