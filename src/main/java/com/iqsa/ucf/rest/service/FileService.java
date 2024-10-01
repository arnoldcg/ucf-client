package com.iqsa.ucf.rest.service;

import com.iqsa.ucf.rest.model.to.DocumentTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file, String passwordUsedAsSecretKey) throws IOException;

    Object[] downloadDocument(DocumentTO data) throws IOException;
}
