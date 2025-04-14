package io.mingachevir.mingachevirrealestateserver.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    ResponseEntity<String> uploadFile(MultipartFile file) throws IOException;
    ResponseEntity<Void> deleteFile(String filePath) throws IOException;
}
