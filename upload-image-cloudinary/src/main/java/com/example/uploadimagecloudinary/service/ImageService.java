package com.example.uploadimagecloudinary.service;

import com.example.uploadimagecloudinary.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadImage(MultipartFile multipartFile) throws IOException;

    Image getImageById(Long id);
}
