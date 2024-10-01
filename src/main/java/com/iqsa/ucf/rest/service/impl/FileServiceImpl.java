package com.iqsa.ucf.rest.service.impl;

import com.iqsa.ucf.rest.crypto.ECBCrypto;
import com.iqsa.ucf.rest.service.FileService;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public FileServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
            var keyHash = passwordEncoder.encode(passwordUsedAsSecretKey);
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
}
