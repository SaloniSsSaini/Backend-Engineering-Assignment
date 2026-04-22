package com.app.adapter.controller;

import com.app.domain.model.Post;
import com.app.infrastructure.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository repo;

    @PostMapping
    public Post create(@RequestBody Post p) {
        return repo.save(p);
    }
}