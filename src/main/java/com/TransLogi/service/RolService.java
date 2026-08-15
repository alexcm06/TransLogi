//package com.TransLogi.service;
//
//import com.TransLogi.domain.Rol;
//import com.TransLogi.repository.RolRepository;
//import java.util.List;
//import java.util.Optional;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class RolService {
//
//    private final RolRepository rolRepository;
//    public RolService(RolRepository rolRepository) {
//        this.rolRepository = rolRepository;
//    }
//
//    @Transactional(readOnly = true)
//    public List<Rol> getRoles() {
//        return rolRepository.findAll();
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<Rol> getRol(Integer idRol) {
//        return rolRepository.findById(idRol);
//    }
//
//    @Transactional
//    public void save(Rol rol) {
//        rolRepository.save(rol);
//    }
//}