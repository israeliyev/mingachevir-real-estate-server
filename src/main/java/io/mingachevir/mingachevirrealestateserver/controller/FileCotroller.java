package io.mingachevir.mingachevirrealestateserver.controller;

import io.mingachevir.mingachevirrealestateserver.service.FileService;
import io.mingachevir.mingachevirrealestateserver.util.CustomServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileCotroller {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteImage(@RequestParam("filePath") String filePath) throws IOException {
        fileService.deleteFile(filePath);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(CustomServiceException.class)
    public ResponseEntity<String> handleCustomException(CustomServiceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage() + ": " + ex.getMessage());
    }
}
