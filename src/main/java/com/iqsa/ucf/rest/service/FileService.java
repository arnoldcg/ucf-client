package com.iqsa.ucf.rest.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file, String passwordUsedAsSecretKey) throws IOException;

}
