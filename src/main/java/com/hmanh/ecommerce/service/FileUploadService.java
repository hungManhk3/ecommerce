package com.hmanh.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    String upload(MultipartFile file);
    boolean delete(String imgUrl);
    List<String> uploadFiles(List<MultipartFile> files);
}
