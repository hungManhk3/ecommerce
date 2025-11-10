package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/files")
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileUploadService.upload(file);
        return ResponseEntity.ok(fileUrl);
    }
    @PostMapping("/upload-multiple")
    public ResponseEntity<List<String>> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        List<String> fileUrls = fileUploadService.uploadFiles(files);
        return ResponseEntity.ok(fileUrls);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("url") String fileUrl) {
        boolean deleted = fileUploadService.delete(fileUrl);
        if (deleted) {
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete file");
        }
    }
}
