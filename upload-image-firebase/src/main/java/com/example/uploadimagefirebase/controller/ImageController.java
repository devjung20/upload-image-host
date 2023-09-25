package com.example.uploadimagefirebase.controller;

import com.example.uploadimagefirebase.enity.ImageEntity;
import com.example.uploadimagefirebase.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping()
    public ResponseEntity<List<ImageEntity>> getAllImages() {
        List<ImageEntity> imageList = imageService.getAllImageUrls();
        return ResponseEntity.ok(imageList);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = imageService.save(file);
            String imageUrl = imageService.getImageUrl(fileName);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteImage(@PathVariable String name) {
        try {
            imageService.delete(name);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
