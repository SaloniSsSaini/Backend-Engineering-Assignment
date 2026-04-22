package com.app.adapter.controller;

import com.app.adapter.dto.CommentDTO;
import com.app.adapter.dto.CreatePostDTO;
import com.app.application.service.CommentService;
import com.app.application.service.PostService;
import com.app.application.service.RateLimitService;
import com.app.domain.model.Comment;
import com.app.domain.model.Post;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RateLimitService rateLimitService;

    // ✅ Create Post
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostDTO dto) {

        // Rate limit check
        if (!rateLimitService.allowed(dto.getAuthorId())) {
            return ResponseEntity.status(429).body("Rate limit exceeded");
        }

        Post post = postService.createPost(dto);

        return ResponseEntity.ok(post);
    }

    // ✅ Add Comment (Human or Bot)
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentDTO dto) {

        if (!rateLimitService.allowed(dto.getAuthorId())) {
            return ResponseEntity.status(429).body("Rate limit exceeded");
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthorId(dto.getAuthorId());
        comment.setContent(dto.getContent());
        comment.setDepthLevel(dto.getDepthLevel());

        if (dto.isBot()) {
            commentService.addBotComment(comment);
        } else {
            commentService.addHumanComment(comment);
        }

        return ResponseEntity.ok("Comment added");
    }

    // ✅ Like Post
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        if (!rateLimitService.allowed(userId)) {
            return ResponseEntity.status(429).body("Rate limit exceeded");
        }

        postService.likePost(postId, userId);

        return ResponseEntity.ok("Post liked");
    }
}