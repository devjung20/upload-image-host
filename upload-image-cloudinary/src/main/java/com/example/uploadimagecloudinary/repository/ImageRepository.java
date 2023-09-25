package com.example.uploadimagecloudinary.repository;

import com.example.uploadimagecloudinary.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
}
