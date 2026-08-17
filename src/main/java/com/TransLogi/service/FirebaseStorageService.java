package com.TransLogi.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseStorageService {
    @Value("${firebase.bucket.name}")
    private String bucketName;
    @Value("${firebase.storage.path}")
    private String storagePath;
    // Cliente configurado en StorageConfig con credenciales externas.
    private final Storage storage;

    public FirebaseStorageService(Storage storage) {
        this.storage = storage;
    }

    // Sube una imagen a Firebase Storage y devuelve su URL firmada.
    public String uploadImage(MultipartFile localFile, String folder, Integer id) throws IOException {
        String originalName = localFile.getOriginalFilename();
        String fileExtension = "";
        if (originalName != null && originalName.contains(".")) {
            fileExtension = originalName.substring(originalName.lastIndexOf("."));
        }

        // Genera un nombre consistente para reemplazar imagenes del mismo registro.
        String fileName = "img" + getFormattedNumber(id) + fileExtension;

        File tempFile = convertToFile(localFile);

        try {
            return uploadToFirebase(tempFile, folder, fileName);
        } finally {
            // El archivo temporal se elimina aunque falle la subida.
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    // Convierte un MultipartFile a un archivo temporal en el servidor.
     private File convertToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("upload-", ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    // Sube el archivo y genera una URL firmada para mostrarlo en la app.
    private String uploadToFirebase(File file, String folder, String fileName) throws IOException {
        // Define la ubicacion final dentro del bucket.
        BlobId blobId = BlobId.of(bucketName, storagePath + "/" + folder + "/" + fileName);
        String mimeType = Files.probeContentType(file.toPath());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(mimeType != null ? mimeType : "media").build();

        // El objeto storage ya tiene las credenciales necesarias.
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        // La URL dura 5 anos para evitar exponer archivos publicos.
        return storage.signUrl(blobInfo, 1825, TimeUnit.DAYS).toString();
    }

    /**
     * Genera un identificador numerico de 14 digitos con ceros a la izquierda.
     */
    private String getFormattedNumber(long id) {
        return String.format("%014d", id);
    }
}
