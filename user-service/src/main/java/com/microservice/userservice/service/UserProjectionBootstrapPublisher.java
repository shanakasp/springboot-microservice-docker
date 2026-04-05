package com.microservice.userservice.service;

import com.microservice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProjectionBootstrapPublisher {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    @EventListener(ApplicationReadyEvent.class)
    public void publishExistingUsers() {
        userRepository.findAll().forEach(userEventPublisher::publishUpdated);
    }
}
