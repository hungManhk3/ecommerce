package com.hmanh.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String upload(MultipartFile file);
    boolean delete(String imgUrl);
}
