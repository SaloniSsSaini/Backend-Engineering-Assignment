package com.app.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private Long authorId;

    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
}