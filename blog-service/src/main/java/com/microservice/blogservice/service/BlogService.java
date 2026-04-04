package com.microservice.blogservice.service;

import com.microservice.blogservice.client.UserClient;
import com.microservice.blogservice.model.Blog;
import com.microservice.blogservice.model.BlogDTO;
import com.microservice.blogservice.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserClient userClient;

    public BlogDTO.BlogResponse createBlog(BlogDTO.CreateBlogRequest request) {
        // Validate user exists via user-service HTTP call
        if (!userClient.userExists(request.getCreatedBy())) {
            throw new RuntimeException("User not found with id: " + request.getCreatedBy());
        }

        Blog blog = new Blog();
        blog.setBlogName(request.getBlogName());
        blog.setContent(request.getContent());
        blog.setCreatedBy(request.getCreatedBy());

        Blog saved = blogRepository.save(blog);
        return BlogDTO.BlogResponse.fromBlog(saved);
    }

    public List<BlogDTO.BlogResponse> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(BlogDTO.BlogResponse::fromBlog)
                .collect(Collectors.toList());
    }

    public List<BlogDTO.BlogResponse> getBlogsByUser(Long userId) {
        return blogRepository.findByCreatedBy(userId)
                .stream()
                .map(BlogDTO.BlogResponse::fromBlog)
                .collect(Collectors.toList());
    }
}
