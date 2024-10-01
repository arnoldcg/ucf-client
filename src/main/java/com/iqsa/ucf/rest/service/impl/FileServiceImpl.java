package com.iqsa.ucf.rest.service.impl;

import com.iqsa.ucf.rest.crypto.ECBCrypto;
import com.iqsa.ucf.rest.model.to.DocumentTO;
import com.iqsa.ucf.rest.service.DocumentService;
import com.iqsa.ucf.rest.service.FileService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

@Service("FileServiceImpl")
public class FileServiceImpl implements FileService {

    private final Path root = Paths.get("uploads");
    private final PasswordEncoder passwordEncoder;
    private final DocumentService documentService;

    public FileServiceImpl(PasswordEncoder passwordEncoder, DocumentService documentService) {
        this.passwordEncoder = passwordEncoder;
        this.documentService = documentService;
    }

    @PostConstruct
    void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String passwordUsedAsSecretKey) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        var pathEncodedFile = this.root.resolve(Objects.requireNonNull(file.getOriginalFilename() + ".enc"));
        var encryptedFileName = pathEncodedFile.toAbsolutePath().toUri().getPath();
        try {
            var keyHash = "zyY\\\"*la>Dw0)Ms$$2Uk`Vx]7I:w+adnr";
            var key = Arrays.copyOf(keyHash.getBytes(StandardCharsets.UTF_8), 32);
            ECBCrypto.encryptWitEcb(file.getInputStream(), encryptedFileName, key);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }

        return encryptedFileName;
    }


    @Override
    public Object[] downloadDocument(DocumentTO data) throws IOException {
        var result = new Object[2];
        var fileLength = 0l;
        ByteArrayResource byteArrayResource;

        var metadataOnDb = this.documentService.getById(data.getId());
        this.validatePassword(data.getPassword(), metadataOnDb.getPassword());

        var fileNameDecrypted = metadataOnDb.getAbsolutePath().replace(".enc","");
        try {
            var keyHash = "zyY\\\"*la>Dw0)Ms$$2Uk`Vx]7I:w+adnr";
            var key = Arrays.copyOf(keyHash.getBytes(StandardCharsets.UTF_8), 32);
            ECBCrypto.decryptWithEcb(metadataOnDb.getAbsolutePath(), fileNameDecrypted, key);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }

        var file = new File(fileNameDecrypted);
        FileInputStream fileInputStream = new FileInputStream(file);
        long byteLength = file.length(); // byte count of the file-content

        byte[] filecontent = new byte[(int) byteLength];
        fileInputStream.read(filecontent, 0, (int) byteLength);
        byteArrayResource = new ByteArrayResource(filecontent);
        fileLength = byteLength;

        result[0] = fileLength;
        result[1] = byteArrayResource;
        return result;
    }

    private void validatePassword(String plainPassword, String encryptedPassword) {
        if(!this.passwordEncoder.matches(plainPassword, encryptedPassword)) {
            throw new RuntimeException("Invalid password");
        }
    }
}
