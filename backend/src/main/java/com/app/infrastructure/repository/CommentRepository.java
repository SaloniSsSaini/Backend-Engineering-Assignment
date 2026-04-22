package com.app.infrastructure.repository;

import com.app.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByPostId(Long postId);
}