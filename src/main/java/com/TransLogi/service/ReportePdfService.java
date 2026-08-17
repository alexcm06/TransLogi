/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.TransLogi.service;

import com.TransLogi.domain.Viaje;
import java.io.ByteArrayInputStream;
import java.util.List;
/**
 *
 * @author sebas
 */
public interface ReportePdfService {
    
    ByteArrayInputStream exportarPdf(List<Viaje> viajes);

}



