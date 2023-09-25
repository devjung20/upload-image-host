package com.example.uploadimagecloudinary.entity;


import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String publicId;
    private String urlImage;
}
