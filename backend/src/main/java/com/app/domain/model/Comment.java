package com.app.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private Long postId;
    private Long authorId;

    private String content;

    private int depthLevel;

    private LocalDateTime createdAt = LocalDateTime.now();
}