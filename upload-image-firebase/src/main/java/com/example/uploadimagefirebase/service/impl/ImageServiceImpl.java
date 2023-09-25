package com.example.uploadimagefirebase.service.impl;

import com.example.uploadimagefirebase.config.Properties;
import com.example.uploadimagefirebase.enity.ImageEntity;
import com.example.uploadimagefirebase.repository.ImageRepository;
import com.example.uploadimagefirebase.service.ImageService;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final Properties properties;
    private final ImageRepository imageRepository;

    public ImageServiceImpl(Properties properties, ImageRepository imageRepository) {
        this.properties = properties;
        this.imageRepository = imageRepository;
    }

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("firebase-upload-image.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setStorageBucket(properties.getBucketName())
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ImageEntity> getAllImageUrls() {
        return imageRepository.findAll();
    }

    @Override
    public String getImageUrl(String name) {
        return String.format(properties.getImageUrl(), name);
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateFileName(file.getOriginalFilename());
        bucket.create(name, file.getBytes(), file.getContentType());

        ImageEntity image = new ImageEntity();
        image.setName(name);
        image.setImageUrl(getImageUrl(name));
        imageRepository.save(image);

        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {
        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateFileName(originalFileName);
        bucket.create(name, bytes);
        return name;
    }

    @Override
    public void delete(String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        if (StringUtils.isEmpty(name)) {
            throw new IOException("invalid file name");
        }
        Blob blob = bucket.get(name);
        if (blob == null) {
            throw new IOException("file not found");
        }
        blob.delete();
    }
}
