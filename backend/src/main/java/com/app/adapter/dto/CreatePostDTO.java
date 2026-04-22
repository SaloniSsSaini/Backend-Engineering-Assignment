package com.app.adapter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePostDTO {

    @NotNull(message = "AuthorId required")
    private Long authorId;

    @NotBlank(message = "Content cannot be empty")
    private String content;
}