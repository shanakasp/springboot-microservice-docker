package com.microservice.blogservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * HTTP client for communicating with the User Service.
 * Used to validate that a user exists before creating a blog.
 *
 * NOTE: Both services share the same DB (tightly coupled), but inter-service
 * communication still happens over HTTP — this is the microservice pattern.
 * In a truly decoupled setup you'd use Feign or Kafka instead.
 */
@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(@Value("${user-service.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Returns true if the user exists in the user-service.
     */
    public boolean userExists(Long userId) {
        try {
            webClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to contact user-service: " + e.getMessage());
        }
    }
}
