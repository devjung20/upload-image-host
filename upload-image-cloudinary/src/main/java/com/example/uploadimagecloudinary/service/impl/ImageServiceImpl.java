package com.example.uploadimagecloudinary.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.uploadimagecloudinary.entity.Image;
import com.example.uploadimagecloudinary.repository.ImageRepository;
import com.example.uploadimagecloudinary.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private Cloudinary cloudinary;

    public ImageServiceImpl(ImageRepository imageRepository, Cloudinary cloudinary) {
        this.imageRepository = imageRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("url");

        Image image = new Image();
        image.setPublicId((String) uploadResult.get("public_id"));
        image.setUrlImage(imageUrl);
        imageRepository.save(image);
        return imageUrl;
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }
}
