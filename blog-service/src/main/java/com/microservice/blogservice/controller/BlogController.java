package com.microservice.blogservice.controller;

import com.microservice.blogservice.model.BlogDTO;
import com.microservice.blogservice.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // POST /api/blogs — Create a new blog
    @PostMapping
    public ResponseEntity<BlogDTO.BlogResponse> createBlog(
            @Valid @RequestBody BlogDTO.CreateBlogRequest request) {
        BlogDTO.BlogResponse response = blogService.createBlog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/blogs — Get all blogs
    @GetMapping
    public ResponseEntity<List<BlogDTO.BlogResponse>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    // GET /api/blogs/user/{userId} — Get all blogs by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BlogDTO.BlogResponse>> getBlogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(blogService.getBlogsByUser(userId));
    }
}
