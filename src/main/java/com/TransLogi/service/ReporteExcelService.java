package com.TransLogi.service;

import com.TransLogi.domain.Viaje;
import java.io.ByteArrayInputStream;
import java.util.List;

public interface ReporteExcelService {

    ByteArrayInputStream exportarExcel(List<Viaje> viajes);

}
