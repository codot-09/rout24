package com.example.rout24.entity;

import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "banners")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String coverImage;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String description;

    private String externalLink;

    @CreationTimestamp
    private LocalDate createdAt;

}
