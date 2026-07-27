package com.TransLogi.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoService {

    // Carpeta donde se almacenarán las licencias
    private final String RUTA_LICENCIAS = "uploads/licencias/";

    /**
     * Guarda una imagen y devuelve el nombre con el que quedó almacenada.
     */
    public String guardarImagen(MultipartFile archivo) throws IOException {

        if (archivo.isEmpty()) {
            return null;
        }

        // Crear la carpeta si no existe
        Path carpeta = Paths.get(RUTA_LICENCIAS);

        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }

        // Obtener extensión del archivo
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";

        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }

        // Generar un nombre único
        String nombreArchivo = UUID.randomUUID().toString() + extension;

        // Guardar el archivo
        Path rutaArchivo = carpeta.resolve(nombreArchivo);

        Files.copy(
                archivo.getInputStream(),
                rutaArchivo,
                StandardCopyOption.REPLACE_EXISTING
        );

        return nombreArchivo;
    }

    /**
     * Elimina una imagen del disco.
     */
    public void eliminarImagen(String nombreArchivo) throws IOException {

        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            return;
        }

        Path ruta = Paths.get(RUTA_LICENCIAS + nombreArchivo);

        Files.deleteIfExists(ruta);
    }
}