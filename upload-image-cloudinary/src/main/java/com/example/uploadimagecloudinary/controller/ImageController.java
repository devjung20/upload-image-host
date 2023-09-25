package com.example.uploadimagecloudinary.controller;

import com.example.uploadimagecloudinary.entity.Image;
import com.example.uploadimagecloudinary.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api/images")
@ControllerAdvice
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String imageUrl = imageService.uploadImage(multipartFile);
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        Image image = imageService.getImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image);
    }
}
