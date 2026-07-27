package com.TransLogi.repository;

import com.TransLogi.domain.RutaCamino;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaCaminoRepository extends JpaRepository<RutaCamino, Integer> {
    
    public List<RutaCamino> findAllByOrderByRequiereRolAsc();
    
}