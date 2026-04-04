package com.microservice.blogservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class BlogDTO {

    @Data
    public static class CreateBlogRequest {

        @NotBlank(message = "Blog name is required")
        private String blogName;

        @NotBlank(message = "Content is required")
        private String content;

        @NotNull(message = "createdBy (user ID) is required")
        private Long createdBy;
    }

    @Data
    public static class BlogResponse {
        private Long id;
        private String blogName;
        private String content;
        private Long createdBy;
        private String createdAt;

        public static BlogResponse fromBlog(Blog blog) {
            BlogResponse response = new BlogResponse();
            response.setId(blog.getId());
            response.setBlogName(blog.getBlogName());
            response.setContent(blog.getContent());
            response.setCreatedBy(blog.getCreatedBy());
            response.setCreatedAt(blog.getCreatedAt() != null ? blog.getCreatedAt().toString() : null);
            return response;
        }
    }
}
